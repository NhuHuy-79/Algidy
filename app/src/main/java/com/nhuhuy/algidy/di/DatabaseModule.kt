import androidx.room.Room
import com.nhuhuy.algidy.core.database.AlgidyDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AlgidyDatabase::class.java,
            "algidy-database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<AlgidyDatabase>().foodDao() }
    single { get<AlgidyDatabase>().wasteDao() }
}