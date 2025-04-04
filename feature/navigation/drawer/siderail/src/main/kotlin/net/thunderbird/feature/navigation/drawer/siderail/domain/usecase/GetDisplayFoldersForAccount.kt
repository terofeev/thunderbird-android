package net.thunderbird.feature.navigation.drawer.siderail.domain.usecase

import app.k9mail.legacy.ui.folder.DisplayFolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import net.thunderbird.feature.navigation.drawer.siderail.domain.DomainContract
import net.thunderbird.feature.navigation.drawer.siderail.domain.entity.DisplayAccountFolder
import net.thunderbird.feature.navigation.drawer.siderail.domain.entity.DisplayFolder
import net.thunderbird.feature.navigation.drawer.siderail.domain.entity.DisplayUnifiedFolder
import net.thunderbird.feature.navigation.drawer.siderail.domain.entity.DisplayUnifiedFolderType

internal class GetDisplayFoldersForAccount(
    private val displayFolderRepository: DisplayFolderRepository,
    private val unifiedFolderRepository: DomainContract.UnifiedFolderRepository,
) : DomainContract.UseCase.GetDisplayFoldersForAccount {
    override fun invoke(accountId: String, includeUnifiedFolders: Boolean): Flow<List<DisplayFolder>> {
        val accountFoldersFlow: Flow<List<DisplayFolder>> =
            displayFolderRepository.getDisplayFoldersFlow(accountId).map { displayFolders ->
                displayFolders.map { displayFolder ->
                    DisplayAccountFolder(
                        accountId = accountId,
                        folder = displayFolder.folder,
                        isInTopGroup = displayFolder.isInTopGroup,
                        unreadMessageCount = displayFolder.unreadMessageCount,
                        starredMessageCount = displayFolder.starredMessageCount,
                    )
                }
            }

        val unifiedFoldersFlow: Flow<List<DisplayFolder>> = if (includeUnifiedFolders) {
            unifiedFolderRepository.getDisplayUnifiedFolderFlow(DisplayUnifiedFolderType.INBOX)
                .map { displayUnifiedFolder ->
                    listOf(displayUnifiedFolder)
                }
        } else {
            flowOf(emptyList<DisplayUnifiedFolder>())
        }

        return combine(
            accountFoldersFlow,
            unifiedFoldersFlow,
        ) { accountFolders, unifiedFolders ->
            unifiedFolders + accountFolders
        }
    }
}
