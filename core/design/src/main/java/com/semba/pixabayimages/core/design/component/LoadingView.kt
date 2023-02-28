package com.semba.pixabayimages.core.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.semba.pixabayimages.core.design.R

@Composable
fun LoadingView(modifier: Modifier = Modifier, size: Dp = 50.dp, showText: Boolean = true) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
        CircularProgressIndicator(modifier = Modifier.
        size(size).
        testTag("loading_view"))
        if (showText) {
            Text(
                text = stringResource(id = R.string.loading),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}