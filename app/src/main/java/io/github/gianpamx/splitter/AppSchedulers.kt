package io.github.gianpamx.splitter

import io.reactivex.Scheduler

interface AppSchedulers {
  fun computation(): Scheduler
  fun mainThread(): Scheduler
}
