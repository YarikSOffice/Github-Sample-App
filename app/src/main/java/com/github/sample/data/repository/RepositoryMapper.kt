package com.github.sample.data.repository

import com.github.sample.data.common.OwnerEntity
import com.github.sample.data.common.RepoEntity
import com.github.sample.domain.model.Owner
import com.github.sample.domain.model.Repo
import javax.inject.Inject

class RepositoryMapper @Inject constructor() {

    fun mapTo(repo: RepoEntity): Repo {
        val ownerEntity = repo.owner
        val owner = Owner(ownerEntity.login, ownerEntity.url, ownerEntity.avatarUrl)
        return Repo(
            repo.id,
            repo.name,
            repo.fullName,
            repo.description,
            owner,
            repo.stars,
            repo.url
        )
    }

    fun mapFrom(repo: Repo): RepoEntity {
        val owner = repo.owner
        val ownerEntity = OwnerEntity(owner.login, owner.url, owner.avatarUrl)
        return RepoEntity(
            repo.id,
            repo.name,
            repo.fullName,
            repo.description,
            ownerEntity,
            repo.stars,
            repo.url
        )
    }
}
