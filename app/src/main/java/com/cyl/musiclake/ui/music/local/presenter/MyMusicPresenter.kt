package com.cyl.musiclake.ui.music.local.presenter

import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.data.PlayHistoryLoader
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.download.DownloadLoader
import com.cyl.musiclake.ui.OnlinePlaylistUtils
import com.cyl.musiclake.ui.music.local.contract.MyMusicContract
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.ToastUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by yonglong on 2018/1/6.
 */

class MyMusicPresenter @Inject
constructor() : BasePresenter<MyMusicContract.View>(), MyMusicContract.Presenter {
    /**
     * 更新播放历史
     */
    fun updateHistory() {
        doAsync {
            val data = PlayHistoryLoader.getPlayHistory()
            uiThread {
                mView?.showHistory(data)
            }
        }
    }

    /**
     * 更新播放历史
     */
    private fun updateLocal() {
        doAsync {
            val data = SongLoader.getLocalMusic(mView.context)
            uiThread {
                mView?.showSongs(data)
            }
        }
    }

    /**
     * 更新本地歌单
     */
    fun updateFavorite() {
        doAsync {
            val data = SongLoader.getFavoriteSong()
            uiThread {
                mView?.showLoveList(data)
            }
        }
    }


    /**
     * 更新本地歌单
     */
    fun updateDownload() {
        doAsync {
            val data = DownloadLoader.getDownloadList()
            uiThread {
                mView?.showDownloadList(data)
            }
        }
    }

    override fun loadSongs() {
        updateLocal()
        updateHistory()
        updateFavorite()
        updateDownload()
    }

    override fun loadPlaylist(playlist: Playlist?) {
        val mIsLogin = UserStatus.getLoginStatus()
        if (mIsLogin) {
            OnlinePlaylistUtils.getOnlinePlaylist(success = {
                mView?.showPlaylist(OnlinePlaylistUtils.playlists)
            }, fail = {
                ToastUtils.show(it)
                mView?.showPlaylist(OnlinePlaylistUtils.playlists)
            })
        } else {
            OnlinePlaylistUtils.playlists.clear()
            mView?.showPlaylist(OnlinePlaylistUtils.playlists)
        }
    }
}
