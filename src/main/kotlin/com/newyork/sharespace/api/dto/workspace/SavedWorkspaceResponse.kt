package com.newyork.sharespace.api.dto.workspace

import java.time.LocalDateTime
import java.util.*

data class SavedWorkspaceResponse(
    val id: UUID,
    val workspace: WorkspaceResponse,
    val savedAt: LocalDateTime
)
