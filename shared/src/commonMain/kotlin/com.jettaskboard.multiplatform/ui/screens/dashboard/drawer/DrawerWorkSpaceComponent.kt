package com.jettaskboard.multiplatform.ui.screens.dashboard.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jettaskboard.multiplatform.ui.components.workspace.WorkSpaceItem
import com.jettaskboard.multiplatform.ui.screens.dashboard.DashboardViewModel

@Composable
fun DrawerWorkSpaceComponent(
    viewModel: DashboardViewModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Workspaces",
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500
        )

        viewModel.listOfBoards.forEach { items ->
            WorkSpaceItem(
                modifier = Modifier.clickable { },
                workSpaceHeading = items.title
            )
        }
    }
}
