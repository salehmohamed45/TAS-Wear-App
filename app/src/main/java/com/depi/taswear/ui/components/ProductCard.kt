package com.depi.taswear.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.depi.taswear.data.model.Product
import com.depi.taswear.ui.theme.AccentColor

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Product image
            AsyncImage(
                model = product.imageUrls.firstOrNull() ?: "",
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Product name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Brand name
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price
                    Text(
                        text = "$${String.format("%.2f", product.price)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = AccentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    // Stock badge
                    when {
                        product.stock == 0 -> {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color.Red.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "Out of Stock",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        product.stock < 5 -> {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = AccentColor.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "Low Stock",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AccentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
