package org.prisma.zenhubetl.dto

class ZenhubConfig {

    String githubOwner
    String githubRepoName
    String githubAccessToken
    String zenhubRepoId
    String zenhubAccessToken

    List<MapEntry> prioritiesMap = []
    List<MapEntry> statusMap = []


}
