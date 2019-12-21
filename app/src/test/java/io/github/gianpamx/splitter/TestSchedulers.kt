package io.github.gianpamx.splitter

class TestSchedulers : AppSchedulers {
  override fun mainThread() = io.reactivex.schedulers.Schedulers.trampoline()
  override fun computation() = io.reactivex.schedulers.Schedulers.trampoline()
}
