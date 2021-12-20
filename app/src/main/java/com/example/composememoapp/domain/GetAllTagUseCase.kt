package com.example.composememoapp.domain

import com.example.composememoapp.data.repository.MemoAppRepository
import javax.inject.Inject

class GetAllTagUseCase @Inject constructor(
    private val memoAppRepository: MemoAppRepository
) {
    operator fun invoke() = memoAppRepository.getAllTag()
}
