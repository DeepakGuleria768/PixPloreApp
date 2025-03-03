package com.example.pixplore.data.mapper

import com.example.pixplore.data.Local.entity.FavouriteImageEntity
import com.example.pixplore.data.Local.entity.UnsplashImageEntity
import com.example.pixplore.data.remote.dto.UnsplashImageDto
import com.example.pixplore.domain.model.UnSplashImage


 fun UnsplashImageDto.toDomainModel(): UnSplashImage {
        return UnSplashImage(
            id = this.id,
            imageUrlSmall = this.urls.small,
            imageUrlRegular = this.urls.regular,
            imageUrlRaw = this.urls.raw,
            photographerName = this.user.name,
            photographerUserName = this.user.username,
            photographerProfileImageUrl = this.user.profile_image.small,
            photographerProfileLink = this.user.links.html,
            width = this.width, height = this.height,
            description = description,
            )
    }
fun UnsplashImageDto.toEntity(): UnsplashImageEntity {
    return UnsplashImageEntity(
        id = this.id,
        imageUrlSmall = this.urls.small,
        imageUrlRegular = this.urls.regular,
        imageUrlRaw = this.urls.raw,
        photographerName = this.user.name,
        photographerUserName = this.user.username,
        photographerProfileImageUrl = this.user.profile_image.small,
        photographerProfileLink = this.user.links.html,
        width = this.width, height = this.height,
        description = description,
    )
}

fun List<UnsplashImageDto>.toEntityList(): List<UnsplashImageEntity> {
    return this.map { it.toEntity() }
}

    fun List<UnsplashImageDto>.toDomainModelList(): List<UnSplashImage> {
        return this.map { it.toDomainModel() }
    }

fun UnSplashImage.toFavouriteImageEntity() : FavouriteImageEntity {
    return FavouriteImageEntity(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUserName = this.photographerUserName,
        photographerProfileImageUrl = this.photographerProfileImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width, height = this.height,
        description = description,
    )
}

fun FavouriteImageEntity.toDomainModel(): UnSplashImage {
    return UnSplashImage(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUserName = this.photographerUserName,
        photographerProfileImageUrl = this.photographerProfileImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width, height = this.height,
        description = description,
    )
}

fun UnsplashImageEntity.toDomainModel(): UnSplashImage {
    return UnSplashImage(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUserName = this.photographerUserName,
        photographerProfileImageUrl = this.photographerProfileImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width, height = this.height,
        description = description,
    )
}



