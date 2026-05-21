package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.model.error_handling.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber

class CleanUpFileWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val appDispatchers: AppDispatchers,
    private val localMediaStorage: LocalMediaStorage,
    private val foodRepository: FoodRepository,
    private val workerScheduler: WorkerScheduler,
) : CoroutineWorker(appContext = context, params = workerParameters) {

    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            try {
                val usedImageList: List<String> = foodRepository.getFoodUriList()
                val usedImageSet = usedImageList.map { it.trim().lowercase() }.toHashSet()
                when (val uriPathInInternalStorage = localMediaStorage.getAllUriPath()) {
                    is Resource.Success -> {
                        val unUsedImageList: List<String> =
                            uriPathInInternalStorage.data.filterNot { uri ->
                                usedImageSet.contains(uri.trim().lowercase())
                            }

                        supervisorScope {
                            unUsedImageList.forEach { uri ->
                                launch {
                                    localMediaStorage.deleteImageFromInternalStorage(uri)
                                }
                            }
                        }
                    }

                    else -> Result.failure()
                }

                Result.success()
            } catch (e: Exception) {
                Timber.e(e)
                Result.retry()
            } finally {
                workerScheduler.scheduleWeeklyCleanUpFileWorker()
            }
        }
    }
}