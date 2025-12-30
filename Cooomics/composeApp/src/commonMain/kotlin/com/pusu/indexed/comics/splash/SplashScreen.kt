package com.pusu.indexed.comics.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 启动页
 * 
 * 显示应用 Logo 和名称，带有渐显动画
 * 
 * @param onSplashFinished 启动页完成后的回调
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // 透明度动画
    val alpha = remember { Animatable(0f) }
    
    // 启动效果：渐显 + 延迟
    LaunchedEffect(Unit) {
        // 渐显动画（800ms）
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
        
        // 停留 1.2 秒
        delay(500)
        
        // 完成
        onSplashFinished()
    }
    
    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.alpha(alpha.value)
        ) {
            // Logo 区域（可以替换为实际的 Logo 图片）
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Color.White.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.large
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎬",
                    fontSize = 64.sp
                )
            }
            
            // 应用名称
            Text(
                text = "Cooomics",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            // 副标题
            Text(
                text = "动漫追番神器",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

