package com.khalidtouch.streamer.core.designsystem.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import com.khalidtouch.streamer.core.database.dao.AlbumDao
import com.khalidtouch.streamer.core.database.dao.ArtistDao
import com.khalidtouch.streamer.core.database.dao.PlaylistDao
import com.khalidtouch.streamer.core.database.dao.SongDao
import com.khalidtouch.streamer.core.database.models.Info
import com.khalidtouch.streamer.core.database.models.Playlist
import com.khalidtouch.streamer.core.database.utils.PlaylistSortBy
import com.khalidtouch.streamer.core.database.utils.SortOrder
import com.khalidtouch.streamer.core.designsystem.R
import com.khalidtouch.streamer.core.designsystem.theme.Dimensions
import com.khalidtouch.streamer.core.designsystem.theme.LocalAppearance
import com.khalidtouch.streamer.core.designsystem.theme.favoritesIcon
import com.khalidtouch.streamer.core.designsystem.theme.px
import com.khalidtouch.streamer.core.domain.extensions.thumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MediaItemMenu(
    onDismiss: () -> Unit,
    mediaItem: MediaItem,
    modifier: Modifier = Modifier,
    onGoToEqualizer: (() -> Unit)? = null,
    onShowSleepTimer: (() -> Unit)? = null,
    onStartRadio: (() -> Unit)? = null,
    onPlayNext: (() -> Unit)? = null,
    onEnqueue: (() -> Unit)? = null,
    onHideFromDatabase: (() -> Unit)? = null,
    onRemoveFromQueue: (() -> Unit)? = null,
    onRemoveFromPlaylist: (() -> Unit)? = null,
    onAddToPlaylist: ((Playlist, Int) -> Unit)? = null,
    onGoToAlbum: ((String) -> Unit)? = null,
    onGoToArtist: ((String) -> Unit)? = null,
    onRemoveFromQuickPicks: (() -> Unit)? = null,
    onShare: () -> Unit,
) {
    val (colorPalette) = LocalAppearance.current
    val density = LocalDensity.current

    var isViewingPlaylists by remember {
        mutableStateOf(false)
    }

    var height by remember {
        mutableStateOf(0.dp)
    }

    var albumInfo by remember {
        mutableStateOf(mediaItem.mediaMetadata.extras?.getString("albumId")?.let { albumId ->
            Info(albumId, null)
        })
    }

    var artistsInfo by remember {
        mutableStateOf(
            mediaItem.mediaMetadata.extras?.getStringArrayList("artistNames")?.let { artistNames ->
                mediaItem.mediaMetadata.extras?.getStringArrayList("artistIds")?.let { artistIds ->
                    artistNames.zip(artistIds).map { (authorName, authorId) ->
                        Info(authorId, authorName)
                    }
                }
            }
        )
    }


    var likedAt by remember {
        mutableStateOf<Long?>(null)
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            if (albumInfo == null) albumInfo = AlbumDao.songAlbumInfo(mediaItem.mediaId)
            if (artistsInfo == null) artistsInfo = ArtistDao.songArtistInfo(mediaItem.mediaId)
            SongDao.likedAt(mediaItem.mediaId).collect { likedAt = it }
        }
    }

    AnimatedContent(
        targetState = isViewingPlaylists,
        transitionSpec = {
            val animationSpec = tween<IntOffset>(400)
            val slideDirection =
                if (targetState) AnimatedContentScope.SlideDirection.Left else AnimatedContentScope.SlideDirection.Right

            slideIntoContainer(slideDirection, animationSpec) with
                    slideOutOfContainer(slideDirection, animationSpec)
        }
    ) { currentlyViewingPlaylists ->
        if (currentlyViewingPlaylists) {
            val playlistPreviews by remember {
                PlaylistDao.playlistPreviews(PlaylistSortBy.DateAdded, SortOrder.Descending)
            }.collectAsState(initial = emptyList(), context = Dispatchers.IO)

            var isCreatingNewPlaylist by rememberSaveable {
                mutableStateOf(false)
            }

            if(isCreatingNewPlaylist && onAddToPlaylist != null) {
                TextFieldDialog(
                    hintText = stringResource(id = R.string.enter_playlist_name),
                    onDismiss = { isCreatingNewPlaylist = false },
                    onDone = { text ->
                        onDismiss()
                        onAddToPlaylist(Playlist(name = text), 0)
                    }
                )
            }

            BackHandler {
                isViewingPlaylists = false
            }


            Menu(
                modifier = modifier
                    .requiredHeight(height)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { isViewingPlaylists = false },
                        icon = R.drawable.chevron_back,
                        color = colorPalette.textSecondary,
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .size(20.dp)
                    )

                    if (onAddToPlaylist != null) {
                        SecondaryTextButton(
                            text = stringResource(id = R.string.new_playlist),
                            onClick = { isCreatingNewPlaylist = true },
                            alternative = true
                        )
                    }
                }

                onAddToPlaylist?.let { onAddToPlaylist ->
                    playlistPreviews.forEach { playlistPreview ->
                        MenuEntry(
                            icon = R.drawable.playlist,
                            text = playlistPreview.playlist.name,
                            secondaryText = "${playlistPreview.songCount} songs",
                            onClick = {
                                onDismiss()
                                onAddToPlaylist(playlistPreview.playlist, playlistPreview.songCount)
                            }
                        )
                    }
                }
            }
        } else {
            Menu(
                modifier = modifier
                    .onPlaced { height = with(density) { it.size.height.toDp() } }
            ) {
                val thumbnailSizeDp = Dimensions.thumbnails.song
                val thumbnailSizePx = thumbnailSizeDp.px

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(end = 12.dp)
                ) {
                    SongItem(
                        thumbnailUrl = mediaItem.mediaMetadata.artworkUri.thumbnail(thumbnailSizePx)
                            ?.toString(),
                        title = mediaItem.mediaMetadata.title.toString(),
                        authors = mediaItem.mediaMetadata.artist.toString(),
                        duration = null,
                        thumbnailSizeDp = thumbnailSizeDp,
                        modifier = Modifier
                            .weight(1f)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            icon = if (likedAt == null) R.drawable.heart_outline else R.drawable.heart,
                            color = colorPalette.favoritesIcon,
                            onClick = {
                                query {
                                    if (Database.like(
                                            mediaItem.mediaId,
                                            if (likedAt == null) System.currentTimeMillis() else null
                                        ) == 0
                                    ) {
                                        Database.insert(mediaItem, Song::toggleLike)
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .size(18.dp)
                        )

                        IconButton(
                            icon = R.drawable.share_social,
                            color = colorPalette.text,
                            onClick = onShare,
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .size(17.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )

                Spacer(
                    modifier = Modifier
                        .alpha(0.5f)
                        .align(Alignment.CenterHorizontally)
                        .background(colorPalette.textDisabled)
                        .height(1.dp)
                        .fillMaxWidth(1f)
                )

                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )

                onStartRadio?.let { onStartRadio ->
                    MenuEntry(
                        icon = R.drawable.radio,
                        text = "Start radio",
                        onClick = {
                            onDismiss()
                            onStartRadio()
                        }
                    )
                }

                onPlayNext?.let { onPlayNext ->
                    MenuEntry(
                        icon = R.drawable.play_skip_forward,
                        text = "Play next",
                        onClick = {
                            onDismiss()
                            onPlayNext()
                        }
                    )
                }

                onEnqueue?.let { onEnqueue ->
                    MenuEntry(
                        icon = R.drawable.enqueue,
                        text = "Enqueue",
                        onClick = {
                            onDismiss()
                            onEnqueue()
                        }
                    )
                }

                onGoToEqualizer?.let { onGoToEqualizer ->
                    MenuEntry(
                        icon = R.drawable.equalizer,
                        text = "Equalizer",
                        onClick = {
                            onDismiss()
                            onGoToEqualizer()
                        }
                    )
                }

                // TODO: find solution to this shit
                onShowSleepTimer?.let {
                    val binder = LocalPlayerServiceBinder.current
                    val (_, typography) = LocalAppearance.current

                    var isShowingSleepTimerDialog by remember {
                        mutableStateOf(false)
                    }

                    val sleepTimerMillisLeft by (binder?.sleepTimerMillisLeft
                        ?: flowOf(null))
                        .collectAsState(initial = null)

                    if (isShowingSleepTimerDialog) {
                        if (sleepTimerMillisLeft != null) {
                            ConfirmationDialog(
                                text = "Do you want to stop the sleep timer?",
                                cancelText = "No",
                                confirmText = "Stop",
                                onDismiss = { isShowingSleepTimerDialog = false },
                                onConfirm = {
                                    binder?.cancelSleepTimer()
                                    onDismiss()
                                }
                            )
                        } else {
                            DefaultDialog(
                                onDismiss = { isShowingSleepTimerDialog = false }
                            ) {
                                var amount by remember {
                                    mutableStateOf(1)
                                }

                                BasicText(
                                    text = "Set sleep timer",
                                    style = typography.s.semiBold,
                                    modifier = Modifier
                                        .padding(vertical = 8.dp, horizontal = 24.dp)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        space = 16.dp,
                                        alignment = Alignment.CenterHorizontally
                                    ),
                                    modifier = Modifier
                                        .padding(vertical = 16.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .alpha(if (amount <= 1) 0.5f else 1f)
                                            .clip(CircleShape)
                                            .clickable(enabled = amount > 1) { amount-- }
                                            .size(48.dp)
                                            .background(colorPalette.background0)
                                    ) {
                                        BasicText(
                                            text = "-",
                                            style = typography.xs.semiBold
                                        )
                                    }

                                    Box(contentAlignment = Alignment.Center) {
                                        BasicText(
                                            text = "88h 88m",
                                            style = typography.s.semiBold,
                                            modifier = Modifier
                                                .alpha(0f)
                                        )
                                        BasicText(
                                            text = "${amount / 6}h ${(amount % 6) * 10}m",
                                            style = typography.s.semiBold
                                        )
                                    }

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .alpha(if (amount >= 60) 0.5f else 1f)
                                            .clip(CircleShape)
                                            .clickable(enabled = amount < 60) { amount++ }
                                            .size(48.dp)
                                            .background(colorPalette.background0)
                                    ) {
                                        BasicText(
                                            text = "+",
                                            style = typography.xs.semiBold
                                        )
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    DialogTextButton(
                                        text = "Cancel",
                                        onClick = { isShowingSleepTimerDialog = false }
                                    )

                                    DialogTextButton(
                                        text = "Set",
                                        enabled = amount > 0,
                                        primary = true,
                                        onClick = {
                                            binder?.startSleepTimer(amount * 10 * 60 * 1000L)
                                            isShowingSleepTimerDialog = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    MenuEntry(
                        icon = R.drawable.alarm,
                        text = "Sleep timer",
                        onClick = { isShowingSleepTimerDialog = true },
                        trailingContent = sleepTimerMillisLeft?.let {
                            {
                                BasicText(
                                    text = "${formatAsDuration(it)} left",
                                    style = typography.xxs.medium,
                                    modifier = modifier
                                        .background(
                                            color = colorPalette.background0,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .animateContentSize()
                                )
                            }
                        }
                    )
                }

                if (onAddToPlaylist != null) {
                    MenuEntry(
                        icon = R.drawable.playlist,
                        text = "Add to playlist",
                        onClick = { isViewingPlaylists = true },
                        trailingContent = {
                            Image(
                                painter = painterResource(R.drawable.chevron_forward),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    colorPalette.textSecondary
                                ),
                                modifier = Modifier
                                    .size(16.dp)
                            )
                        }
                    )
                }

                onGoToAlbum?.let { onGoToAlbum ->
                    albumInfo?.let { (albumId) ->
                        MenuEntry(
                            icon = R.drawable.disc,
                            text = "Go to album",
                            onClick = {
                                onDismiss()
                                onGoToAlbum(albumId)
                            }
                        )
                    }
                }

                onGoToArtist?.let { onGoToArtist ->
                    artistsInfo?.forEach { (authorId, authorName) ->
                        MenuEntry(
                            icon = R.drawable.person,
                            text = "More of $authorName",
                            onClick = {
                                onDismiss()
                                onGoToArtist(authorId)
                            }
                        )
                    }
                }

                onRemoveFromQueue?.let { onRemoveFromQueue ->
                    MenuEntry(
                        icon = R.drawable.trash,
                        text = "Remove from queue",
                        onClick = {
                            onDismiss()
                            onRemoveFromQueue()
                        }
                    )
                }

                onRemoveFromPlaylist?.let { onRemoveFromPlaylist ->
                    MenuEntry(
                        icon = R.drawable.trash,
                        text = "Remove from playlist",
                        onClick = {
                            onDismiss()
                            onRemoveFromPlaylist()
                        }
                    )
                }

                onHideFromDatabase?.let { onHideFromDatabase ->
                    MenuEntry(
                        icon = R.drawable.trash,
                        text = "Hide",
                        onClick = onHideFromDatabase
                    )
                }

                onRemoveFromQuickPicks?.let {
                    MenuEntry(
                        icon = R.drawable.trash,
                        text = "Hide from \"Quick picks\"",
                        onClick = {
                            onDismiss()
                            onRemoveFromQuickPicks()
                        }
                    )
                }
            }
        }
    }
}