package com.smic.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smic.data.db.AppDao
import com.smic.data.db.AppDatabase
import com.smic.data.entities.FavoriteVerbsOfRoom
import com.smic.data.entities.SettingsOfRoom
import com.smic.data.entities.VerbOfRoom
import com.smic.data.repositories.LocalRepository
import com.smic.domain.entities.FavoriteVerbs
import com.smic.domain.entities.Settings
import com.smic.domain.entities.Verb
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Smogevscih Yuri
 * 07.07.2022
 */
@RunWith(AndroidJUnit4::class)
class LocalRepositoryTest {
    private lateinit var subject: LocalRepository
    private lateinit var dao: AppDao
    private lateinit var db: AppDatabase
    private lateinit var verbOfRoom_test: VerbOfRoom
    private lateinit var verb_test: Verb
    private val SIZE_OF_VERBS = 50


    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.getDao()

        verbOfRoom_test = Helper.verbOfRoom_test
        verb_test = Helper.verb_test

        subject = LocalRepository(dao)

        dao.insert(verbOfRoom_test)
        Helper.getHundredVerbs().forEach {
            dao.insert(it)
        }

        dao.insert(Helper.getSettingsOfRoom())
        dao.insert(Helper.favoriteVerbsOfRoom)

    }

    @Test
    fun test_getVerb() {
        val infinitivo = verb_test.infinitivo

        val verbOfRoom = dao.getVerb(infinitivo)
        Truth.assertThat(verbOfRoom).isEqualTo(verbOfRoom_test)

        val verb = subject.getVerb(infinitivo)
        Truth.assertThat(verb).isEqualTo(verb_test)
    }

    @Test
    fun test_getAllVerbs() {
        val verbs = subject.getAllVerbs()
        Truth.assertThat(verbs.size).isEqualTo(100)
        Truth.assertThat(verbs).isInstanceOf(List::class.java)
        Truth.assertThat(verbs.first()).isInstanceOf(Verb::class.java)
    }

    @Test
    fun test_getVerbs_with_quest() {
        var quest = "v"
        var verbs = subject.getVerbs(quest)
        Truth.assertThat(verbs.size).isEqualTo(SIZE_OF_VERBS)

        quest = "c"
        verbs = subject.getVerbs(quest)
        Truth.assertThat(verbs.size).isEqualTo(0)
    }

    @Test
    fun test_getNextVerbs() {
        val quest = "v"
        val verbs = subject.getVerbs(quest)
        val nextVerbs = subject.getNextVerbs(quest, 50)

        Truth.assertThat(verbs.size).isEqualTo(50)
        Truth.assertThat(verbs).isNotEqualTo(nextVerbs)
    }

    @Test
    fun test_getSettings() {
        val settings = subject.getSettings()
        Truth.assertThat(settings).isEqualTo(Helper.getSettings())
    }

    @Test
    fun test_Settings_after_modify_pronombres() {
        val settings = subject.getSettings()
        val keys = settings.pronombresSettings.keys
        keys.forEach { settings.pronombresSettings[it] = false }
        subject.updateSettings(settings)

        val settingsAfterUpdate = subject.getSettings()
        Truth.assertThat(settings).isEqualTo(settingsAfterUpdate)
    }

    @Test
    fun test_getFavoriteVerbs() {
        val favoriteVerbs = subject.getFavoriteVerbs()
        Truth.assertThat(favoriteVerbs).isEqualTo(Helper.favoriteVerbs)
    }

    @Test
    fun test_updateFavoriteVerbs() {
        var favoriteVerbs = subject.getFavoriteVerbs()
        val newFavoriteVerbs = favoriteVerbs.copy(favoriteVerbs = listOf("essere"))
        subject.updateFavoriteVerbs(newFavoriteVerbs)
        favoriteVerbs = subject.getFavoriteVerbs()
        Truth.assertThat(favoriteVerbs).isEqualTo(newFavoriteVerbs)

    }

    @After
    fun closeVb() {
        db.close()
    }
}

object Helper {
    private val gson = Gson()
    val verb_json_test =
        "{\"infinitivo\":\"vivere\",\"listConjugado\":[{\"nombre\":\"Indicativo presente\",\"mapConjugado\":{\"io\":\"vivo\",\"tu\":\"vivi\",\"lui/lei\":\"vive\",\"noi\":\"viviamo\",\"voi\":\"vivete\",\"loro\":\"vivono\"}},{\"nombre\":\"Indicativo passato prossimo\",\"mapConjugado\":{\"io\":\"ho vissuto\",\"tu\":\"hai vissuto\",\"lui/lei\":\"ha vissuto\",\"noi\":\"abbiamo vissuto\",\"voi\":\"avete vissuto\",\"loro\":\"hanno vissuto\"}},{\"nombre\":\"Indicativo imperfetto\",\"mapConjugado\":{\"io\":\"vivevo\",\"tu\":\"vivevi\",\"lui/lei\":\"viveva\",\"noi\":\"vivevamo\",\"voi\":\"vivevate\",\"loro\":\"vivevano\"}},{\"nombre\":\"Indicativo trapassato prossimo\",\"mapConjugado\":{\"io\":\"avevo vissuto\",\"tu\":\"avevi vissuto\",\"lui/lei\":\"aveva vissuto\",\"noi\":\"avevamo vissuto\",\"voi\":\"avevate vissuto\",\"loro\":\"avevano vissuto\"}},{\"nombre\":\"Indicativo passato remoto\",\"mapConjugado\":{\"io\":\"vissi\",\"tu\":\"vivesti\",\"lui/lei\":\"visse\",\"noi\":\"vivemmo\",\"voi\":\"viveste\",\"loro\":\"vissero\"}},{\"nombre\":\"Indicativo trapassato remoto\",\"mapConjugado\":{\"io\":\"ebbi vissuto\",\"tu\":\"avesti vissuto\",\"lui/lei\":\"ebbe vissuto\",\"noi\":\"avemmo vissuto\",\"voi\":\"aveste vissuto\",\"loro\":\"ebbero vissuto\"}},{\"nombre\":\"Indicativo futuro semplice\",\"mapConjugado\":{\"io\":\"vivrò\",\"tu\":\"vivrai\",\"lui/lei\":\"vivrà\",\"noi\":\"vivremo\",\"voi\":\"vivrete\",\"loro\":\"vivranno\"}},{\"nombre\":\"Indicativo futuro anteriore\",\"mapConjugado\":{\"io\":\"avrò vissuto\",\"tu\":\"avrai vissuto\",\"lui/lei\":\"avrà vissuto\",\"noi\":\"avremo vissuto\",\"voi\":\"avrete vissuto\",\"loro\":\"avranno vissuto\"}},{\"nombre\":\"Congiuntivo presente\",\"mapConjugado\":{\"io\":\"viva\",\"tu\":\"viva\",\"lui/lei\":\"viva\",\"noi\":\"viviamo\",\"voi\":\"viviate\",\"loro\":\"vivano\"}},{\"nombre\":\"Congiuntivo passato\",\"mapConjugado\":{\"io\":\"abbia vissuto\",\"tu\":\"abbia vissuto\",\"lui/lei\":\"abbia vissuto\",\"noi\":\"abbiamo vissuto\",\"voi\":\"abbiate vissuto\",\"loro\":\"abbiano vissuto\"}},{\"nombre\":\"Congiuntivo imperfetto\",\"mapConjugado\":{\"io\":\"vivessi\",\"tu\":\"vivessi\",\"lui/lei\":\"vivesse\",\"noi\":\"vivessimo\",\"voi\":\"viveste\",\"loro\":\"vivessero\"}},{\"nombre\":\"Congiuntivo trapassato\",\"mapConjugado\":{\"io\":\"avessi vissuto\",\"tu\":\"avessi vissuto\",\"lui/lei\":\"avesse vissuto\",\"noi\":\"avessimo vissuto\",\"voi\":\"aveste vissuto\",\"loro\":\"avessero vissuto\"}},{\"nombre\":\"Condizionale presente\",\"mapConjugado\":{\"io\":\"vivrei\",\"tu\":\"vivresti\",\"lui/lei\":\"vivrebbe\",\"noi\":\"vivremmo\",\"voi\":\"vivreste\",\"loro\":\"vivrebbero\"}},{\"nombre\":\"Condizionale passato\",\"mapConjugado\":{\"io\":\"avrei vissuto\",\"tu\":\"avresti vissuto\",\"lui/lei\":\"avrebbe vissuto\",\"noi\":\"avremmo vissuto\",\"voi\":\"avreste vissuto\",\"loro\":\"avrebbero vissuto\"}},{\"nombre\":\"Imperativo\",\"mapConjugado\":{\"io\":\"\",\"tu\":\"vivi\",\"lui/lei\":\"viva\",\"noi\":\"viviamo\",\"voi\":\"vivete\",\"loro\":\"vivano\"}},{\"nombre\":\"Infinito\",\"mapConjugado\":{\"vivere\":\"\"}},{\"nombre\":\"Infinito passato\",\"mapConjugado\":{\"avere vissuto\":\"\"}},{\"nombre\":\"Participio presente\",\"mapConjugado\":{\"vivente\":\"\"}},{\"nombre\":\"Participio passato\",\"mapConjugado\":{\"vissuto\":\"\"}},{\"nombre\":\"Gerundio\",\"mapConjugado\":{\"vivendo\":\"\"}},{\"nombre\":\"Gerundio passato\",\"mapConjugado\":{\"avendo vissuto\":\"\"}}],\"translationEN\":\"live\",\"translationRU\":\"жить\"}"
    val verb_test = gson.fromJson(verb_json_test, Verb::class.java)
    val verbOfRoom_test = VerbOfRoom(1, "vivere", verb_json_test)
    val favoriteVerbs = FavoriteVerbs(listOf("vivire"))
    val favoriteVerbsOfRoom = FavoriteVerbsOfRoom(1, "[\"vivire\"]")

    val pronombresJson = "{\n" +
            "  \"io\": true,\n" +
            "  \"tu\": true,\n" +
            "  \"lui/lei\": true,\n" +
            "  \"noi\": true,\n" +
            "  \"voi\": true,\n" +
            "  \"loro\": true\n" +
            "}"

    val tensesJson =
        "[{\"first\":\"Indicativo presente\",\"second\":true},{\"first\":\"Indicativo passato prossimo\",\"second\":true},{\"first\":\"Indicativo imperfetto\",\"second\":true},{\"first\":\"Indicativo trapassato prossimo\",\"second\":true},{\"first\":\"Indicativo passato remoto\",\"second\":true},{\"first\":\"Indicativo trapassato remoto\",\"second\":true},{\"first\":\"Indicativo futuro semplice\",\"second\":true},{\"first\":\"Indicativo futuro anteriore\",\"second\":true},{\"first\":\"Congiuntivo presente\",\"second\":true},{\"first\":\"Congiuntivo passato\",\"second\":true},{\"first\":\"Congiuntivo imperfetto\",\"second\":true},{\"first\":\"Congiuntivo trapassato\",\"second\":true},{\"first\":\"Condizionale presente\",\"second\":true},{\"first\":\"Condizionale passato\",\"second\":true},{\"first\":\"Imperativo\",\"second\":true},{\"first\":\"Infinito\",\"second\":true},{\"first\":\"Infinito passato\",\"second\":true},{\"first\":\"Participio presente\",\"second\":true},{\"first\":\"Participio passato\",\"second\":true},{\"first\":\"Gerundio\",\"second\":true},{\"first\":\"Gerundio passato\",\"second\":true}]"

    fun getSettings(): Settings {
        val pronombres =
            gson.fromJson<MutableMap<String, Boolean>>(pronombresJson, MutableMap::class.java)
        val listType = object : TypeToken<List<Pair<String, Boolean>>>() {}.type
        val tenses =
            gson.fromJson<MutableList<Pair<String, Boolean>>>(tensesJson, listType)

        return Settings(pronombres, tenses)
    }

    fun getSettingsOfRoom(): SettingsOfRoom {
        return SettingsOfRoom(1, pronombresJson, tensesJson)
    }

    fun getHundredVerbs(): List<VerbOfRoom> {
        return (2..100).map {
            VerbOfRoom(
                it,
                verbOfRoom_test.infinitivo,
                verb_json_test.replace("vivere", "vivire$it")
            )
        }

    }
}