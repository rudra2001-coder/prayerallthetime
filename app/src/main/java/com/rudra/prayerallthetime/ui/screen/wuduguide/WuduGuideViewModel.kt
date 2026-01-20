package com.rudra.prayerallthetime.ui.screen.wuduguide

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WuduGuideViewModel @Inject constructor() : ViewModel() {

    private val _steps = MutableStateFlow(
        listOf(
            WuduStep("1. Niyyah (Intention)", "Make the intention in your heart to perform Wudu for the sake of Allah."),
            WuduStep("2. Bismillah", "Say 'Bismillah' (In the name of Allah) before starting."),
            WuduStep("3. Wash Hands", "Wash your hands up to the wrists three times, making sure to clean between the fingers."),
            WuduStep("4. Rinse Mouth", "Rinse your mouth three times, using your right hand to put water in."),
            WuduStep("5. Clean Nose", "Sniff water into your nostrils and blow it out three times, using your left hand to clear the nose."),
            WuduStep("6. Wash Face", "Wash your entire face three times, from the hairline to the chin and from ear to ear."),
            WuduStep("7. Wash Arms", "Wash your right arm up to and including the elbow three times, then do the same for the left arm."),
            WuduStep("8. Wipe Head", "Wipe your wet hands over your head once, starting from the front to the back and back to the front."),
            WuduStep("9. Clean Ears", "Wipe the inside of your ears with your index fingers and the back with your thumbs once."),
            WuduStep("10. Wash Feet", "Wash your right foot up to and including the ankle three times, cleaning between the toes. Then do the same for the left foot."),
            WuduStep("11. Dua After Wudu", "Recite: 'Ash-hadu alla ilaha illallah wahdahu la sharika lah, wa ash-hadu anna Muhammadan 'abduhu wa rasuluh.'")
        )
    )
    val steps: StateFlow<List<WuduStep>> = _steps.asStateFlow()
}
