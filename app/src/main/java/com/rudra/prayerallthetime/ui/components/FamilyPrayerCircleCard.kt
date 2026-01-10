package com.rudra.prayerallthetime.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rudra.prayerallthetime.data.FamilyMember

@Composable
fun FamilyPrayerCircleCard(onClick: () -> Unit) {
    val familyMembers = listOf(
        FamilyMember(id = "1", name = "You", role = "Admin", avatarEmoji = "👤", prayersCompleted = 5),
        FamilyMember(id = "2", name = "Father", role = "Member", avatarEmoji = "👨", prayersCompleted = 4),
        FamilyMember(id = "3", name = "Mother", role = "Member", avatarEmoji = "👩", prayersCompleted = 5),
        FamilyMember(id = "4", name = "Sister", role = "Member", avatarEmoji = "👧", prayersCompleted = 3)
    )

    Card(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Family Prayer Circle", modifier = Modifier.padding(bottom = 8.dp))
            familyMembers.forEach { member ->
                FamilyMemberProgress(member)
            }
        }
    }
}

@Composable
fun FamilyMemberProgress(member: FamilyMember) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = member.name, modifier = Modifier.weight(1f))
        LinearProgressIndicator(
            progress = member.prayersCompleted.toFloat() / 5f,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${member.prayersCompleted}/5",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
