// FIXME: When the API is ready, remove Suppress annotation below.
@file:Suppress("UnusedPrivateProperty", "UnusedPrivateMember")

package io.github.droidkaigi.confsched.data.contributors

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import io.github.droidkaigi.confsched.data.NetworkService
import io.github.droidkaigi.confsched.data.contributors.response.ContributorsResponse
import io.github.droidkaigi.confsched.model.Contributor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

internal interface ContributorApi {
    @GET("/events/droidkaigi2024/contributors")
    suspend fun getContributors(): ContributorsResponse
}

public class DefaultContributorsApiClient(
    private val networkService: NetworkService,
    ktorfit: Ktorfit,
) : ContributorsApiClient {

    private val contributorApi = ktorfit.create<ContributorApi>()
    public override suspend fun contributors(): PersistentList<Contributor> {
        return networkService {
            contributorApi.getContributors()
        }.toContributorList()
    }
}

public interface ContributorsApiClient {

    public suspend fun contributors(): PersistentList<Contributor>
}

private fun ContributorsResponse.toContributorList(): PersistentList<Contributor> {
    return contributors.map {
        Contributor(
            id = it.id,
            username = it.username,
            profileUrl = "https://github.com/${it.username}",
            iconUrl = it.iconUrl,
        )
    }.toPersistentList()
}
