package com.learnwithsubs.word_list.domain.usecase

data class WordListUseCases(
    val getWordListUseCase: GetWordListUseCase,
    val loadWordUseCase: LoadWordUseCase,
    val deleteWordUseCase: DeleteWordUseCase,
    val sortWordListUseCases: SortWordListUseCase,
    val filterWordListUseCase: FilterWordListUseCase,
    val getWordsListSortedByVideoIdUseCase: GetWordsListSortedByVideoIdUseCase,
)