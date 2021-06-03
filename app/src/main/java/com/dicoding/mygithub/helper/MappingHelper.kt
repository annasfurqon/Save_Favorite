package com.dicoding.mygithub.helper

import android.database.Cursor
import com.dicoding.mygithub.UserData
import com.dicoding.mygithub.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArray(userCursor: Cursor?): ArrayList<UserData> {
        val list = ArrayList<UserData>()

        userCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
                val repository = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))
                val followers = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING))
                val favorite = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FAVORITE))
                list.add(
                    UserData(
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository,
                        followers,
                        following,
                        favorite
                    )
                )
            }
        }
        return list
    }
}