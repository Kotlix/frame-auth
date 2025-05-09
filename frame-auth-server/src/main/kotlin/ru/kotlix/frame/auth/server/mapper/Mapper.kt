package ru.kotlix.frame.auth.server.mapper

import ru.kotlix.frame.auth.api.dto.FullProfileInfoDto
import ru.kotlix.frame.auth.api.dto.ProfileInfoDto
import ru.kotlix.frame.auth.server.service.dto.DetailProfileInfo
import ru.kotlix.frame.auth.server.service.dto.ProfileInfo

fun DetailProfileInfo.toFullProfileInfoDto() =
    FullProfileInfoDto(
        login = login,
        username = username,
        email = email,
    )

fun ProfileInfo.toProfileInfoDto() =
    ProfileInfoDto(
        username = username,
    )
