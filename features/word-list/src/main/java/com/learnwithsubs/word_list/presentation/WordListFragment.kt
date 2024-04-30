package com.learnwithsubs.word_list.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.base.OnSelectChange
import com.example.base.OnSelectionModeChange
import com.learnwithsubs.word_list.R
import com.learnwithsubs.word_list.databinding.DialogWordListMenuSortByBinding
import com.learnwithsubs.word_list.databinding.FragmentWordListBinding
import com.learnwithsubs.word_list.databinding.SearchViewBinding
import com.learnwithsubs.word_list.di.DaggerWordListAppComponent
import com.learnwithsubs.word_list.di.WordListAppModule
import com.learnwithsubs.word_list.presentation.adapter.WordListTitleAdapter
import javax.inject.Inject

class WordListFragment : Fragment(), OnSelectChange {
    @Inject
    lateinit var vmFactory: WordListViewModelFactory
    private lateinit var vm: WordListViewModel

    private lateinit var fragmentWordListBinding: FragmentWordListBinding
    private lateinit var searchViewBinding: SearchViewBinding
    private lateinit var sortByDialogBinding: DialogWordListMenuSortByBinding

    private lateinit var context: Context

    private val adapter = WordListTitleAdapter()
    private var selectionMode: OnSelectionModeChange? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        context = requireContext()
        DaggerWordListAppComponent.builder().wordListAppModule(WordListAppModule(context = context)).build().inject(this)
        vm = ViewModelProvider(this, vmFactory)[WordListViewModel::class.java]

        val videoListActivity = requireActivity()
        fragmentWordListBinding = FragmentWordListBinding.inflate(inflater, container, false)
        sortByDialogBinding = DialogWordListMenuSortByBinding.inflate(videoListActivity.layoutInflater)
        searchViewBinding = fragmentWordListBinding.searchBar
        val editDialog = EditDialog(fragment = this@WordListFragment, vm = vm, adapter = adapter)
        setupRecyclerView()


        vm.wordList.observe(viewLifecycleOwner) { wordList ->
            wordList ?: return@observe
            adapter.updateData(ArrayList(wordList))
        }

        fragmentWordListBinding.closeSelectionMode.setOnClickListener{
            adapter.changeMode(isSelectionMode = false)
        }
        fragmentWordListBinding.deSelectAllMenu.setOnClickListener {
            val isSelectAll = adapter.getChildSelectedItemsSize() == adapter.getChildItemListSize()
            if (isSelectAll)
                adapter.deselectAll()
            else
                adapter.selectAll()
        }
        fragmentWordListBinding.deleteMenu.setOnClickListener {
            vm.deleteWords(selectedWords = adapter.getChildSelectedItems())
        }
        fragmentWordListBinding.editMenu.setOnClickListener {
            vm.editableWord = adapter.getEditableItem()
            editDialog.openEditMenu()
        }
        fragmentWordListBinding.addWordCard.setOnClickListener {
            vm.editableWord = null
            editDialog.openEditMenu()
        }
        /*
        fragmentWordListBinding.sortBy.setOnClickListener {
            openSortByMenu()
        }*/

        searchViewBinding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (adapter.getIsSelectionMode()) return
                vm.setFilterMode(filter = s.toString())
                val data = vm.wordList.value ?: return
                adapter.updateData(ArrayList(vm.filterVideoList(data)))
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return fragmentWordListBinding.root
    }


    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        fragmentWordListBinding.wordList.layoutManager = layoutManager
        fragmentWordListBinding.wordList.adapter = adapter
        adapter.setListener(this)
        (fragmentWordListBinding.wordList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun changeCardVisibility(cardView: CardView, isVisible: Boolean) {
        cardView.alpha = if (isVisible) 1f else 0.4f
        cardView.isClickable = isVisible
        cardView.isFocusable = isVisible
    }

    override fun onModeChange(isSelectionMode: Boolean) {
        val visibleInNormalMode = if (!isSelectionMode) View.VISIBLE else View.GONE
        val visibleInSelectionMode = if (isSelectionMode) View.VISIBLE else View.GONE

        searchViewBinding.searchEditText.isEnabled = !isSelectionMode
        searchViewBinding.searchEditText.isClickable = !isSelectionMode
        searchViewBinding.root.alpha = if (!isSelectionMode) 1f else 0.6f

        fragmentWordListBinding.closeSelectionMode.visibility = visibleInSelectionMode

        selectionMode?.selectionModeChange(isSelectionMode = isSelectionMode)
        fragmentWordListBinding.editListLayout.visibility = visibleInSelectionMode
        fragmentWordListBinding.addWordCard.visibility = visibleInNormalMode
    }


    // Если выделено всё
    override fun onSelectAll() {
        fragmentWordListBinding.deSelectAllMenuText.text = context.getString(R.string.deselect_all)
        changeCardVisibility(cardView = fragmentWordListBinding.deleteMenu, isVisible = true)
    }

    // Если было выделено всё, а стало на 1 и более меньше
    override fun onDeselectAll() {
        fragmentWordListBinding.deSelectAllMenuText.text = context.getString(R.string.select_all)
    }

    // Если не выделено ничего
    override fun onZeroSelect() {
        fragmentWordListBinding.deSelectAllMenuText.text = context.getString(R.string.select_all)

        changeCardVisibility(cardView = fragmentWordListBinding.deleteMenu, isVisible = false)
        changeCardVisibility(cardView = fragmentWordListBinding.editMenu, isVisible = false)
    }

    // Если выделено только 1 видео
    override fun onSingleSelected() {
        changeCardVisibility(cardView = fragmentWordListBinding.deleteMenu, isVisible = true)
        changeCardVisibility(cardView = fragmentWordListBinding.editMenu, isVisible = true)
    }

    // Если выделено было выделено 1 видео, а стало любое другое число
    override fun onNotSingleSelected() {
        changeCardVisibility(cardView = fragmentWordListBinding.editMenu, isVisible = false)
    }

    override fun onSomeSelect() {
        changeCardVisibility(cardView = fragmentWordListBinding.deleteMenu, isVisible = true)
    }


    fun attachSelectionMode(listener: OnSelectionModeChange) {
        this.selectionMode = listener
    }
}