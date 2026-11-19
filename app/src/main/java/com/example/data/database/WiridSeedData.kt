package com.example.data.database

import com.example.data.model.DhikrEntity

object WiridModuleTypes {
    const val GENERAL = "GENERAL"
    const val AL_GHAZALI = "AL_GHAZALI"
    const val WIRID_PAGI = "WIRID_PAGI"
    const val WIRID_PETANG = "WIRID_PETANG"
    const val SETELAH_SALAT = "SETELAH_SALAT"
    const val ISTIGHFAR = "ISTIGHFAR"
    const val SELAWAT = "SELAWAT"
    const val TASBIH_TAHMID_TAKBIR = "TASBIH_TAHMID_TAKBIR"
    const val WIRID_MALAM = "WIRID_MALAM"
    const val DOA_TIDUR = "DOA_TIDUR"
}

object WiridSourceTypes {
    const val BIDAYATUL_HIDAYAH = "BIDAYATUL_HIDAYAH"
    const val HADIS = "HADIS"
    const val AMALAN_TAMBAHAN = "AMALAN_TAMBAHAN"
    const val KUSTOM = "KUSTOM"
    const val UMUM = "UMUM"
}

object WiridSeedData {

    /**
     * 10 Kalimat Wirid Harian Imam Al-Ghazali
     * Rujukan: Kitab Bidāyatul Hidāyah (Bab Ādāb Ba‘da Shalāt ash-Shubḥ / Tartīb al-Awrād)
     * Dianjurkan dibaca masing-masing 100 kali, 70 kali, atau 10 kali.
     */
    val AL_GHAZALI_WIRIDS = listOf(
        DhikrEntity(
            id = "ghazali_1_tahlil_tauhid",
            name = "1. Kalimat Tauhid Agung",
            arabic = "لَا إِلٰهَ إِلَّا اللّٰهُ وَحْدَهُ لَا شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، يُحْيِيْ وَيُمِيْتُ، وَهُوَ حَيٌّ لَا يَمُوْتُ، بِيَدِهِ الْخَيْرُ، وَهُوَ عَلٰى كُلِّ شَيْءٍ قَدِيْرٌ",
            transliteration = "Lā ilāha illallāhu waḥdahu lā syarīka lah, lahul-mulku wa lahul-ḥamdu, yuḥyī wa yumītu, wa huwa ḥayyun lā yamūt, biyadihil-khairu, wa huwa ‘alā kulli syai’in qadīr.",
            translation = "Tiada tuhan selain Allah Yang Maha Esa, tiada sekutu bagi-Nya. Bagi-Nya kerajaan dan segala puji. Dialah Yang menghidupkan dan mematikan, dan Dia Maha Hidup tidak akan mati. Di tangan-Nya segala kebaikan, dan Dia Mahakuasa atas segala sesuatu.",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = true,
            createdAt = 10100L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 1,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        ),
        DhikrEntity(
            id = "ghazali_2_malikul_haqq",
            name = "2. Al-Malikul Haqqul Mubin",
            arabic = "لَا إِلٰهَ إِلَّا اللّٰهُ الْمَلِكُ الْحَقُّ الْمُبِيْنُ",
            transliteration = "Lā ilāha illallāhul-Malikul-Ḥaqqul-Mubīn.",
            translation = "Tiada tuhan selain Allah, Raja Yang Maha Benar lagi Maha Nyata.",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = false,
            createdAt = 10200L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 2,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        ),
        DhikrEntity(
            id = "ghazali_3_wahidul_qahhar",
            name = "3. Al-Wahidul Qahhar",
            arabic = "لَا إِلٰهَ إِلَّا اللّٰهُ الْوَاحِدُ الْقَهَّارُ، رَبُّ السَّمَاوَاتِ وَالْأَرْضِ وَمَا بَيْنَهُمَا الْعَزِيْزُ الْغَفَّارُ",
            transliteration = "Lā ilāha illallāhul-Wāḥidul-Qahhār, rabbus-samāwāti wal-arḍi wa mā bainahumal-‘Azīzul-Ghaffār.",
            translation = "Tiada tuhan selain Allah Yang Maha Esa lagi Maha Perkasa, Tuhan langit dan bumi serta apa yang ada di antara keduanya, Yang Maha Mulia lagi Maha Pengampun.",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = false,
            createdAt = 10300L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 3,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        ),
        DhikrEntity(
            id = "ghazali_4_baqiyat_shalihat",
            name = "4. Al-Baqiyat ash-Shalihat & Hauqalah",
            arabic = "سُبْحَانَ اللّٰهِ وَالْحَمْدُ لِلّٰهِ وَلَا إِلٰهَ إِلَّا اللّٰهُ وَاللّٰهُ أَكْبَرُ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللّٰهِ الْعَلِيِّ الْعَظِيْمِ",
            transliteration = "Subḥānallāhi wal-ḥamdulillāhi wa lā ilāha illallāhu wallāhu akbar, wa lā ḥaula wa lā quwwata illā billāhil-‘Aliyyil-‘Aẓīm.",
            translation = "Maha Suci Allah, segala puji bagi Allah, tiada tuhan selain Allah, dan Allah Maha Besar. Tiada daya dan kekuatan melainkan dengan pertolongan Allah Yang Maha Tinggi lagi Maha Agung.",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = false,
            createdAt = 10400L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 4,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        ),
        DhikrEntity(
            id = "ghazali_5_subbuhun_quddus",
            name = "5. Subbuhun Quddus",
            arabic = "سُبُّوْحٌ قُدُّوْسٌ رَبُّنَا وَرَبُّ الْمَلَائِكَةِ وَالرُّوْحِ",
            transliteration = "Subbūḥun Quddūsun rabbunā wa rabbul-malā’ikati war-rūḥ.",
            translation = "Maha Suci lagi Maha Kudus Tuhan kami dan Tuhan para malaikat serta Jibril (Ar-Ruh).",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = false,
            createdAt = 10500L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 5,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        ),
        DhikrEntity(
            id = "ghazali_6_subhanallah_bihamdihi",
            name = "6. Tasbih Al-Khafifatan",
            arabic = "سُبْحَانَ اللّٰهِ وَبِحَمْدِهِ سُبْحَانَ اللّٰهِ الْعَظِيْمِ",
            transliteration = "Subḥānallāhi wa biḥamdihi subḥānallāhil-‘Aẓīm.",
            translation = "Maha Suci Allah dengan segala puji bagi-Nya, Maha Suci Allah Yang Maha Agung.",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = false,
            createdAt = 10600L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 6,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        ),
        DhikrEntity(
            id = "ghazali_7_istighfar_kabir",
            name = "7. Istighfar Al-Hayyul Qayyum",
            arabic = "أَسْتَغْفِرُ اللّٰهَ الْعَظِيْمَ الَّذِيْ لَا إِلٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّوْمُ وَأَسْأَلُهُ التَّوْبَةَ وَالْمَغْفِرَةَ",
            transliteration = "Astaghfirullāhal-‘Aẓīmal-ladzī lā ilāha illā huwal-Ḥayyul-Qayyūmu wa as’aluhut-taubata wal-maghfirah.",
            translation = "Aku memohon ampun kepada Allah Yang Maha Agung, tiada tuhan selain Dia Yang Maha Hidup lagi Maha Berdiri Sendiri, dan aku memohon kepada-Nya taubat serta ampunan.",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = false,
            createdAt = 10700L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 7,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        ),
        DhikrEntity(
            id = "ghazali_8_la_mania",
            name = "8. Doa Keteguhan Takdir & Pemberian",
            arabic = "اَللّٰهُمَّ لَا مَانِعَ لِمَا أَعْطَيْتَ، وَلَا مُعْطِيَ لِمَا مَنَعْتَ، وَلَا رَادَّ لِمَا قَضَيْتَ، وَلَا يَنْفَعُ ذَا الْجَدِّ مِنْكَ الْجَدُّ",
            transliteration = "Allāhumma lā māni‘a limā a‘ṭaita, wa lā mu‘ṭiya limā mana‘ta, wa lā rādda limā qaḍaita, wa lā yanfa‘u dzal-jaddi minkal-jadd.",
            translation = "Ya Allah, tiada yang dapat menghalangi apa yang Engkau berikan, tiada yang dapat memberi apa yang Engkau halangi, tiada yang dapat menolak apa yang Engkau tetapkan, dan tiada berguna kekayaan serta kemuliaan seseorang dari siksa-Mu.",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = false,
            createdAt = 10800L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 8,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        ),
        DhikrEntity(
            id = "ghazali_9_shalawat",
            name = "9. Shalawat kepada Nabi ﷺ",
            arabic = "اَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَعَلٰى آلِ مُحَمَّدٍ وَسَلِّمْ",
            transliteration = "Allāhumma ṣalli ‘alā Muḥammadin wa ‘alā āli Muḥammadin wa sallim.",
            translation = "Ya Allah, limpahkanlah rahmat dan keselamatan kepada Nabi Muhammad beserta keluarga Nabi Muhammad.",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = false,
            createdAt = 10900L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 9,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        ),
        DhikrEntity(
            id = "ghazali_10_bismillah_la_yadurru",
            name = "10. Doa Perlindungan Asma Allah",
            arabic = "بِسْمِ اللّٰهِ الَّذِيْ لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيْعُ الْعَلِيْمُ",
            transliteration = "Bismillāhil-ladzī lā yaḍurru ma‘asmihi syai’un fil-arḍi wa lā fis-samā’i wa huwas-Samī‘ul-‘Alīm.",
            translation = "Dengan nama Allah yang bersama nama-Nya tidak ada sesuatu pun di bumi maupun di langit yang dapat membahayakan, dan Dialah Yang Maha Mendengar lagi Maha Mengetahui.",
            category = "Wirid Al-Ghazali",
            target = 100,
            isCustom = false,
            isFavorite = false,
            createdAt = 11000L,
            source = "Kitab Bidāyatul Hidāyah — Imam Al-Ghazali",
            moduleType = WiridModuleTypes.AL_GHAZALI,
            sortOrder = 10,
            sourceType = WiridSourceTypes.BIDAYATUL_HIDAYAH
        )
    )

    val ADDITIONAL_WIRIDS = listOf(
        // ==================== WIRID PAGI ====================
        DhikrEntity(
            id = "pagi_1_sayyidul_istighfar",
            name = "Sayyidul Istighfar",
            arabic = "اَللّٰهُمَّ أَنْتَ رَبِّيْ لَا إِلٰهَ إِلَّا أَنْتَ خَلَقْتَنِيْ وَأَنَا عَبْدُكَ وَأَنَا عَلٰى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوْذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوْءُ لَكَ بِنِعْمَتِكَ عَلَيَّ وَأَبُوْءُ بِذَنْبِيْ فَاغْفِرْ لِيْ فَإِنَّهُ لَا يَغْفِرُ الذُّنُوْبَ إِلَّا أَنْتَ",
            transliteration = "Allāhumma anta rabbī lā ilāha illā anta khalaqtanī wa ana ‘abduka wa ana ‘alā ‘ahdika wa wa‘dika mastaṭa‘tu, a‘ūdzu bika min syarri mā ṣana‘tu, abū’u laka bini‘matika ‘alayya wa abū’u bidzanbī faghfir lī fa’innahu lā yaghfirudz-dzunūba illā anta.",
            translation = "Ya Allah, Engkau adalah Tuhanku, tiada tuhan selain Engkau. Engkau menciptakanku dan aku adalah hamba-Mu. Aku berada di atas perjanjian dan janji-Mu semampuku. Aku berlindung kepada-Mu dari keburukan perbuatanku, aku mengakui nikmat-Mu kepadaku dan dosaku, maka ampunilah aku, karena tiada yang mengampuni dosa selain Engkau.",
            category = "Wirid Pagi",
            target = 1,
            source = "HR. Al-Bukhari no. 6306",
            moduleType = WiridModuleTypes.WIRID_PAGI,
            sortOrder = 1,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 20100L
        ),
        DhikrEntity(
            id = "pagi_2_ashbahna",
            name = "Dzikir Memasuki Waktu Pagi",
            arabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلّٰهِ، وَالْحَمْدُ لِلّٰهِ، لَا إِلٰهَ إِلَّا اللّٰهُ وَحْدَهُ لَا شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلٰى كُلِّ شَيْءٍ قَدِيْرٌ",
            transliteration = "Aṣbaḥnā wa aṣbaḥal-mulku lillāh, wal-ḥamdulillāh, lā ilāha illallāhu waḥdahu lā syarīka lah, lahul-mulku wa lahul-ḥamdu wa huwa ‘alā kulli syai’in qadīr.",
            translation = "Kami memasuki waktu pagi dan kerajaan pun tetap milik Allah, segala puji bagi Allah. Tiada tuhan selain Allah Yang Maha Esa, tiada sekutu bagi-Nya. Milik-Nya kerajaan dan pujian, dan Dia Mahakuasa atas segala sesuatu.",
            category = "Wirid Pagi",
            target = 1,
            source = "HR. Muslim no. 2723",
            moduleType = WiridModuleTypes.WIRID_PAGI,
            sortOrder = 2,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 20200L
        ),
        DhikrEntity(
            id = "pagi_3_radhitu",
            name = "Ikrar Ridha Pagi",
            arabic = "رَضِيْتُ بِاللّٰهِ رَبًّا، وَبِالْإِسْلَامِ دِيْنًا، وَبِمُحَمَّدٍ صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
            transliteration = "Raḍītu billāhi rabbā, wa bil-islāmi dīnā, wa bi-Muḥammadin ṣallallāhu ‘alaihi wa sallama nabiyyā.",
            translation = "Aku ridha Allah sebagai Tuhanku, Islam sebagai agamaku, dan Nabi Muhammad ﷺ sebagai nabiku.",
            category = "Wirid Pagi",
            target = 3,
            source = "HR. Abu Dawud no. 5072 & At-Tirmidzi no. 3389",
            moduleType = WiridModuleTypes.WIRID_PAGI,
            sortOrder = 3,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 20300L
        ),
        DhikrEntity(
            id = "pagi_4_hasbiyallah",
            name = "Hasbiyallahu La Ilaha Illa Huwa",
            arabic = "حَسْبِيَ اللّٰهُ لَا إِلٰهَ إِلَّا هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيْمِ",
            transliteration = "Ḥasbiyallāhu lā ilāha illā huwa ‘alaihi tawakkaltu wa huwa rabbul-‘arsyil-‘aẓīm.",
            translation = "Cukuplah Allah bagiku, tiada tuhan selain Dia. Kepada-Nya aku bertawakal, dan Dialah Tuhan pemilik ‘Arsy yang agung.",
            category = "Wirid Pagi",
            target = 7,
            source = "HR. Abu Dawud no. 5081 (Amalan Pagi & Petang)",
            moduleType = WiridModuleTypes.WIRID_PAGI,
            sortOrder = 4,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 20400L
        ),
        DhikrEntity(
            id = "pagi_5_afiyah",
            name = "Doa Kesehatan Pagi",
            arabic = "اَللّٰهُمَّ عَافِنِيْ فِيْ بَدَنِيْ، اَللّٰهُمَّ عَافِنِيْ فِيْ سَمْعِيْ، اَللّٰهُمَّ عَافِنِيْ فِيْ بَصَرِيْ، لَا إِلٰهَ إِلَّا أَنْتَ",
            transliteration = "Allāhumma ‘āfinī fī badanī, Allāhumma ‘āfinī fī sam‘ī, Allāhumma ‘āfinī fī baṣarī, lā ilāha illā anta.",
            translation = "Ya Allah, sehatkanlah badanku. Ya Allah, sehatkanlah pendengaranku. Ya Allah, sehatkanlah penglihatanku. Tiada tuhan selain Engkau.",
            category = "Wirid Pagi",
            target = 3,
            source = "HR. Abu Dawud no. 5090",
            moduleType = WiridModuleTypes.WIRID_PAGI,
            sortOrder = 5,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 20500L
        ),

        // ==================== WIRID PETANG ====================
        DhikrEntity(
            id = "petang_1_amsaina",
            name = "Dzikir Memasuki Waktu Petang",
            arabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلّٰهِ، وَالْحَمْدُ لِلّٰهِ، لَا إِلٰهَ إِلَّا اللّٰهُ وَحْدَهُ لَا شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلٰى كُلِّ شَيْءٍ قَدِيْرٌ",
            transliteration = "Amsainā wa amsal-mulku lillāh, wal-ḥamdulillāh, lā ilāha illallāhu waḥdahu lā syarīka lah, lahul-mulku wa lahul-ḥamdu wa huwa ‘alā kulli syai’in qadīr.",
            translation = "Kami memasuki waktu petang dan kerajaan pun tetap milik Allah, segala puji bagi Allah. Tiada tuhan selain Allah Yang Maha Esa, tiada sekutu bagi-Nya.",
            category = "Wirid Petang",
            target = 1,
            source = "HR. Muslim no. 2723",
            moduleType = WiridModuleTypes.WIRID_PETANG,
            sortOrder = 1,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 21100L
        ),
        DhikrEntity(
            id = "petang_2_sayyidul_istighfar",
            name = "Sayyidul Istighfar Petang",
            arabic = "اَللّٰهُمَّ أَنْتَ رَبِّيْ لَا إِلٰهَ إِلَّا أَنْتَ خَلَقْتَنِيْ وَأَنَا عَبْدُكَ وَأَنَا عَلٰى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوْذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوْءُ لَكَ بِنِعْمَتِكَ عَلَيَّ وَأَبُوْءُ بِذَنْبِيْ فَاغْفِرْ لِيْ فَإِنَّهُ لَا يَغْفِرُ الذُّنُوْبَ إِلَّا أَنْتَ",
            transliteration = "Allāhumma anta rabbī lā ilāha illā anta khalaqtanī wa ana ‘abduka wa ana ‘alā ‘ahdika wa wa‘dika mastaṭa‘tu...",
            translation = "Ya Allah, Engkau adalah Tuhanku, tiada tuhan selain Engkau... Ampunilah aku, karena tiada yang mengampuni dosa selain Engkau.",
            category = "Wirid Petang",
            target = 1,
            source = "HR. Al-Bukhari no. 6306",
            moduleType = WiridModuleTypes.WIRID_PETANG,
            sortOrder = 2,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 21200L
        ),
        DhikrEntity(
            id = "petang_3_audzu_bikalimatillah",
            name = "Doa Perlindungan Petang",
            arabic = "أَعُوْذُ بِكَلِمَاتِ اللّٰهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
            transliteration = "A‘ūdzu bikalimātillāhit-tāmmāti min syarri mā khalaq.",
            translation = "Aku berlindung dengan kalimat-kalimat Allah yang sempurna dari kejahatan makhluk yang Dia ciptakan.",
            category = "Wirid Petang",
            target = 3,
            source = "HR. Muslim no. 2709",
            moduleType = WiridModuleTypes.WIRID_PETANG,
            sortOrder = 3,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 21300L
        ),
        DhikrEntity(
            id = "petang_4_bika_amsaina",
            name = "Doa Berserah di Waktu Petang",
            arabic = "اَللّٰهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوْتُ، وَإِلَيْكَ الْمَصِيْرُ",
            transliteration = "Allāhumma bika amsainā, wa bika aṣbaḥnā, wa bika naḥyā, wa bika namūtu, wa ilaikal-maṣīr.",
            translation = "Ya Allah, dengan rahmat-Mu kami memasuki petang, dengan rahmat-Mu kami memasuki pagi, dengan rahmat-Mu kami hidup, dengan kehendak-Mu kami mati, dan kepada-Mu tempat kembali.",
            category = "Wirid Petang",
            target = 1,
            source = "HR. At-Tirmidzi no. 3391",
            moduleType = WiridModuleTypes.WIRID_PETANG,
            sortOrder = 4,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 21400L
        ),

        // ==================== ZIKIR SETELAH SALAT FARDU ====================
        DhikrEntity(
            id = "salat_1_istighfar",
            name = "Istighfar Setelah Salam",
            arabic = "أَسْتَغْفِرُ اللّٰهَ",
            transliteration = "Astaghfirullāh (3x)",
            translation = "Aku memohon ampunan kepada Allah.",
            category = "Setelah Salat",
            target = 3,
            source = "HR. Muslim no. 591",
            moduleType = WiridModuleTypes.SETELAH_SALAT,
            sortOrder = 1,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 22100L
        ),
        DhikrEntity(
            id = "salat_2_antas_salam",
            name = "Allahumma Antas-Salam",
            arabic = "اَللّٰهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
            transliteration = "Allāhumma antas-salāmu wa minkas-salām, tabārakta yā dzal-jalāli wal-ikrām.",
            translation = "Ya Allah, Engkaulah Maha Pemberi Kedamaian dan dari-Mu kedamaian berasal. Maha Berkah Engkau, wahai Pemilik keagungan dan kemuliaan.",
            category = "Setelah Salat",
            target = 1,
            source = "HR. Muslim no. 591",
            moduleType = WiridModuleTypes.SETELAH_SALAT,
            sortOrder = 2,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 22200L
        ),
        DhikrEntity(
            id = "salat_3_subhanallah",
            name = "Tasbih Ba'da Salat",
            arabic = "سُبْحَانَ اللّٰهِ",
            transliteration = "Subḥānallāh",
            translation = "Maha Suci Allah",
            category = "Setelah Salat",
            target = 33,
            source = "HR. Muslim no. 597",
            moduleType = WiridModuleTypes.SETELAH_SALAT,
            sortOrder = 3,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 22300L
        ),
        DhikrEntity(
            id = "salat_4_alhamdulillah",
            name = "Tahmid Ba'da Salat",
            arabic = "اَلْحَمْدُ لِلّٰهِ",
            transliteration = "Al-ḥamdulillāh",
            translation = "Segala puji bagi Allah",
            category = "Setelah Salat",
            target = 33,
            source = "HR. Muslim no. 597",
            moduleType = WiridModuleTypes.SETELAH_SALAT,
            sortOrder = 4,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 22400L
        ),
        DhikrEntity(
            id = "salat_5_allahu_akbar",
            name = "Takbir Ba'da Salat",
            arabic = "اَللّٰهُ أَكْبَرُ",
            transliteration = "Allāhu Akbar",
            translation = "Allah Maha Besar",
            category = "Setelah Salat",
            target = 33,
            source = "HR. Muslim no. 597",
            moduleType = WiridModuleTypes.SETELAH_SALAT,
            sortOrder = 5,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 22500L
        ),
        DhikrEntity(
            id = "salat_6_tahlil_100",
            name = "Penyempurna Ke-100 Ba'da Salat",
            arabic = "لَا إِلٰهَ إِلَّا اللّٰهُ وَحْدَهُ لَا شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلٰى كُلِّ شَيْءٍ قَدِيْرٌ",
            transliteration = "Lā ilāha illallāhu waḥdahu lā syarīka lah, lahul-mulku wa lahul-ḥamdu wa huwa ‘alā kulli syai’in qadīr.",
            translation = "Tiada tuhan selain Allah Yang Maha Esa, tiada sekutu bagi-Nya. Bagi-Nya kerajaan dan segala puji, dan Dia Mahakuasa atas segala sesuatu.",
            category = "Setelah Salat",
            target = 1,
            source = "HR. Muslim no. 597",
            moduleType = WiridModuleTypes.SETELAH_SALAT,
            sortOrder = 6,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 22600L
        ),

        // ==================== ISTIGHFAR ====================
        DhikrEntity(
            id = "istighfar_1_ringkas",
            name = "Istighfar Harian",
            arabic = "أَسْتَغْفِرُ اللّٰهَ الْعَظِيْمَ وَأَتُوْبُ إِلَيْهِ",
            transliteration = "Astaghfirullāhal-‘Aẓīma wa atūbu ilaih.",
            translation = "Aku memohon ampun kepada Allah Yang Maha Agung dan aku bertaubat kepada-Nya.",
            category = "Istighfar",
            target = 100,
            source = "HR. Al-Bukhari no. 6307 & Muslim no. 2702",
            moduleType = WiridModuleTypes.ISTIGHFAR,
            sortOrder = 1,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 23100L
        ),
        DhikrEntity(
            id = "istighfar_2_rabbighfirli",
            name = "Istighfar Nabi dalam Majelis",
            arabic = "رَبِّ اغْفِرْ لِيْ وَتُبْ عَلَيَّ إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيْمُ",
            transliteration = "Rabbighfir lī wa tub ‘alayya innaka antat-Tawwābur-Raḥīm.",
            translation = "Wahai Tuhanku, ampunilah aku dan terimalah taubatku. Sesungguhnya Engkau Maha Penerima Taubat lagi Maha Penyayang.",
            category = "Istighfar",
            target = 100,
            source = "HR. Abu Dawud no. 1516 & At-Tirmidzi no. 3434",
            moduleType = WiridModuleTypes.ISTIGHFAR,
            sortOrder = 2,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 23200L
        ),

        // ==================== SELAWAT ====================
        DhikrEntity(
            id = "selawat_1_jibril",
            name = "Shalawat Ringkas (Shallallahu ‘ala Muhammad)",
            arabic = "صَلَّى اللّٰهُ عَلٰى مُحَمَّدٍ",
            transliteration = "Ṣallallāhu ‘alā Muḥammad.",
            translation = "Semoga Allah melimpahkan rahmat kepada Nabi Muhammad.",
            category = "Selawat",
            target = 100,
            source = "Amalan Ulama & Ahlus Sunnah",
            moduleType = WiridModuleTypes.SELAWAT,
            sortOrder = 1,
            sourceType = WiridSourceTypes.AMALAN_TAMBAHAN,
            createdAt = 24100L
        ),
        DhikrEntity(
            id = "selawat_2_ibrahimiyah",
            name = "Shalawat Ibrahimiyah",
            arabic = "اَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيْمَ وَعَلٰى آلِ إِبْرَاهِيْمَ إِنَّكَ حَمِيْدٌ مَجِيْدٌ",
            transliteration = "Allāhumma ṣalli ‘alā Muḥammadin wa ‘alā āli Muḥammadin kamā ṣallaita ‘alā Ibrāhīma wa ‘alā āli Ibrāhīma innaka Ḥamīdun Majīd.",
            translation = "Ya Allah, berilah rahmat kepada Nabi Muhammad dan keluarga Nabi Muhammad sebagaimana Engkau telah memberi rahmat kepada Nabi Ibrahim dan keluarga Nabi Ibrahim. Sesungguhnya Engkau Maha Terpuji lagi Maha Mulia.",
            category = "Selawat",
            target = 10,
            source = "HR. Al-Bukhari no. 3370 & Muslim no. 406",
            moduleType = WiridModuleTypes.SELAWAT,
            sortOrder = 2,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 24200L
        ),

        // ==================== TASBIH, TAHMID, TAKBIR ====================
        DhikrEntity(
            id = "ttt_1_subhanallah",
            name = "Tasbih (Subhanallah)",
            arabic = "سُبْحَانَ اللّٰهِ",
            transliteration = "Subḥānallāh",
            translation = "Maha Suci Allah",
            category = "Tasbih, Tahmid, Takbir",
            target = 33,
            source = "HR. Muslim no. 597",
            moduleType = WiridModuleTypes.TASBIH_TAHMID_TAKBIR,
            sortOrder = 1,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 25100L
        ),
        DhikrEntity(
            id = "ttt_2_alhamdulillah",
            name = "Tahmid (Alhamdulillah)",
            arabic = "اَلْحَمْدُ لِلّٰهِ",
            transliteration = "Al-ḥamdulillāh",
            translation = "Segala puji bagi Allah",
            category = "Tasbih, Tahmid, Takbir",
            target = 33,
            source = "HR. Muslim no. 597",
            moduleType = WiridModuleTypes.TASBIH_TAHMID_TAKBIR,
            sortOrder = 2,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 25200L
        ),
        DhikrEntity(
            id = "ttt_3_allahuakbar",
            name = "Takbir (Allahu Akbar)",
            arabic = "اَللّٰهُ أَكْبَرُ",
            transliteration = "Allāhu Akbar",
            translation = "Allah Maha Besar",
            category = "Tasbih, Tahmid, Takbir",
            target = 34,
            source = "HR. Al-Bukhari no. 3705 & Muslim no. 2727",
            moduleType = WiridModuleTypes.TASBIH_TAHMID_TAKBIR,
            sortOrder = 3,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 25300L
        ),

        // ==================== WIRID MALAM ====================
        DhikrEntity(
            id = "malam_1_terbangun",
            name = "Dzikir Terjaga di Malam Hari",
            arabic = "لَا إِلٰهَ إِلَّا اللّٰهُ وَحْدَهُ لَا شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلٰى كُلِّ شَيْءٍ قَدِيْرٌ، اَلْحَمْدُ لِلّٰهِ وَسُبْحَانَ اللّٰهِ وَلَا إِلٰهَ إِلَّا اللّٰهُ وَاللّٰهُ أَكْبَرُ وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللّٰهِ",
            transliteration = "Lā ilāha illallāhu waḥdahu lā syarīka lah, lahul-mulku wa lahul-ḥamdu wa huwa ‘alā kulli syai’in qadīr. Al-ḥamdulillāhi wa subḥānallāhi wa lā ilāha illallāhu wallāhu akbar wa lā ḥaula wa lā quwwata illā billāh.",
            translation = "Tiada tuhan selain Allah Yang Maha Esa... Segala puji bagi Allah, Maha Suci Allah, tiada tuhan selain Allah, Allah Maha Besar, tiada daya dan upaya selain dengan pertolongan Allah.",
            category = "Wirid Malam",
            target = 1,
            source = "HR. Al-Bukhari no. 1154",
            moduleType = WiridModuleTypes.WIRID_MALAM,
            sortOrder = 1,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 26100L
        ),
        DhikrEntity(
            id = "malam_2_tahlil_malam",
            name = "Tahlil Ketenteraman Malam",
            arabic = "لَا إِلٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّيْ كُنْتُ مِنَ الظَّالِمِيْنَ",
            transliteration = "Lā ilāha illā anta subḥānaka innī kuntu minaẓ-ẓālimīn.",
            translation = "Tiada tuhan selain Engkau, Maha Suci Engkau, sungguh aku termasuk orang-orang yang menzalimi diri sendiri.",
            category = "Wirid Malam",
            target = 33,
            source = "QS. Al-Anbiya: 87 & Amalan Malam Ulama",
            moduleType = WiridModuleTypes.WIRID_MALAM,
            sortOrder = 2,
            sourceType = WiridSourceTypes.AMALAN_TAMBAHAN,
            createdAt = 26200L
        ),

        // ==================== DOA SEBELUM TIDUR ====================
        DhikrEntity(
            id = "tidur_1_bismika",
            name = "Doa Sebelum Tidur",
            arabic = "بِسْمِكَ اللّٰهُمَّ أَمُوْتُ وَأَحْيَا",
            transliteration = "Bismikallāhumma amūtu wa aḥyā.",
            translation = "Dengan nama-Mu ya Allah, aku mati (tidur) dan aku hidup (bangun).",
            category = "Doa Sebelum Tidur",
            target = 1,
            source = "HR. Al-Bukhari no. 6324",
            moduleType = WiridModuleTypes.DOA_TIDUR,
            sortOrder = 1,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 27100L
        ),
        DhikrEntity(
            id = "tidur_2_qini_adzabak",
            name = "Doa Perlindungan Menjelang Tidur",
            arabic = "اَللّٰهُمَّ قِنِيْ عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ",
            transliteration = "Allāhumma qinī ‘adzābaka yauma tab‘atsu ‘ibādak.",
            translation = "Ya Allah, lindungilah aku dari azab-Mu pada hari Engkau membangkitkan seluruh hamba-Mu.",
            category = "Doa Sebelum Tidur",
            target = 3,
            source = "HR. Abu Dawud no. 5045 & At-Tirmidzi no. 3398",
            moduleType = WiridModuleTypes.DOA_TIDUR,
            sortOrder = 2,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 27200L
        ),
        DhikrEntity(
            id = "tidur_3_tasbih_fatimah",
            name = "Tasbih Fathimah Sebelum Tidur",
            arabic = "سُبْحَانَ اللّٰهِ (٣٣) • اَلْحَمْدُ لِلّٰهِ (٣٣) • اَللّٰهُ أَكْبَرُ (٣٤)",
            transliteration = "Subḥānallāh (33x), Al-ḥamdulillāh (33x), Allāhu Akbar (34x)",
            translation = "Membaca tasbih 33 kali, tahmid 33 kali, dan takbir 34 kali sebelum tidur lebih baik daripada seorang pembantu.",
            category = "Doa Sebelum Tidur",
            target = 100,
            source = "HR. Al-Bukhari no. 3705 & Muslim no. 2727",
            moduleType = WiridModuleTypes.DOA_TIDUR,
            sortOrder = 3,
            sourceType = WiridSourceTypes.HADIS,
            createdAt = 27300L
        )
    )
}
