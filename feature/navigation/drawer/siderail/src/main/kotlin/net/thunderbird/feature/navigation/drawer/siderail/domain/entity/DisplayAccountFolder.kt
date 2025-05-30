package net.thunderbird.feature.navigation.drawer.siderail.domain.entity

import net.thunderbird.feature.mail.folder.api.Folder

internal data class DisplayAccountFolder(
    val accountId: String,
    val folder: Folder,
    val isInTopGroup: Boolean,
    override val unreadMessageCount: Int,
    override val starredMessageCount: Int,
) : DisplayFolder {
    override val id: String = createDisplayAccountFolderId(accountId, folder.id)
}

fun createDisplayAccountFolderId(accountId: String, folderId: Long): String {
    return "${accountId}_$folderId"
}
