package com.learnwithsubs.feature_word_list

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.learnwithsubs.OnSelectChange
import com.learnwithsubs.OnSelectParentChange
import com.learnwithsubs.R
import com.learnwithsubs.app.App
import com.learnwithsubs.databinding.ActivityVideoListBinding
import com.learnwithsubs.databinding.DialogWordListMenuEditBinding
import com.learnwithsubs.databinding.DialogWordListMenuSortByBinding
import com.learnwithsubs.databinding.FragmentWordListBinding
import com.learnwithsubs.databinding.SearchViewBinding
import com.learnwithsubs.feature_video_list.VideoListActivity
import com.learnwithsubs.feature_word_list.adapter.WordListAdapter
import com.learnwithsubs.feature_word_list.adapter.WordListTitleAdapter
import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.feature_word_list.util.WordOrder
import com.learnwithsubs.general.util.OrderType
import java.util.Date
import javax.inject.Inject

class WordListFragment : Fragment(), OnSelectChange, OnSelectParentChange {
    @Inject
    lateinit var vmFactory: WordListViewModelFactory
    private lateinit var vm: WordListViewModel

    private lateinit var videoListActivity: VideoListActivity
    private lateinit var fragmentWordListBinding: FragmentWordListBinding
    private lateinit var searchViewBinding: SearchViewBinding
    private lateinit var videoListBinding: ActivityVideoListBinding
    private lateinit var dialogWordListMenuEditBinding: DialogWordListMenuEditBinding
    private lateinit var sortByDialogBinding: DialogWordListMenuSortByBinding


    private val adapter = WordListTitleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        videoListActivity = requireActivity() as VideoListActivity
        fragmentWordListBinding = FragmentWordListBinding.inflate(inflater, container, false)
        dialogWordListMenuEditBinding = DialogWordListMenuEditBinding.inflate(videoListActivity.layoutInflater)
        sortByDialogBinding = DialogWordListMenuSortByBinding.inflate(videoListActivity.layoutInflater)
        searchViewBinding = fragmentWordListBinding.searchBar
        videoListBinding = videoListActivity.videoListBinding
        setupRecyclerView()

        (videoListActivity.applicationContext as App).wordListAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[WordListViewModel::class.java]

        vm.wordList.observe(videoListActivity) { wordList ->
            wordList ?: return@observe
            //val sorted = vm.getSortedVideoList(wordList = wordList)
            adapter.updateData(ArrayList(wordList.toList())) //TODO вызывается несколько раз при переходе на фрагмент
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
            openEditMenu()
        }
        fragmentWordListBinding.addWordCard.setOnClickListener {
            vm.editableWord = null
            openEditMenu()
        }
        fragmentWordListBinding.sortBy.setOnClickListener {
            openSortByMenu()
        }

        searchViewBinding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (adapter.getIsSelectionMode()) return
                vm.setFilterMode(filter = s.toString())
                vm.updateVideoList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return fragmentWordListBinding.root
    }

    private fun openSortByMenu() {
        val sortByDialog = Dialog(videoListActivity)
        sortByDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = sortByDialogBinding.root
        val parentView = dialogView.parent as? ViewGroup
        parentView?.removeView(dialogView)
        sortByDialog.setContentView(dialogView)

        fun setButtonColors(ascending: Boolean) {
            sortByDialogBinding.ascending.setBackgroundColor(if (ascending) videoListActivity.applicationContext.getColor(
                R.color.button_pressed
            ) else videoListActivity.applicationContext.getColor(R.color.button_normal))
            sortByDialogBinding.descending.setBackgroundColor(if (ascending) videoListActivity.applicationContext.getColor(
                R.color.button_normal
            ) else videoListActivity.applicationContext.getColor(R.color.button_pressed))
        }

        sortByDialogBinding.alphabetCheckBox.isChecked = vm.getVideoOrder() is WordOrder.Alphabet
        sortByDialogBinding.dateCheckBox.isChecked = vm.getVideoOrder() is WordOrder.Date
        sortByDialogBinding.videoCheckBox.isChecked = vm.getVideoOrder() is WordOrder.Video

        val sortType = vm.getVideoOrder()
        setButtonColors(ascending = sortType.orderType is OrderType.Ascending)

        sortByDialogBinding.ascending.setOnClickListener {
            setButtonColors(ascending = true)
            vm.setOrderType(newOrderType = OrderType.Ascending)
            val currentVideoOrder = vm.getVideoOrder()
            vm.setVideoOrder(currentVideoOrder)
        }

        sortByDialogBinding.descending.setOnClickListener {
            setButtonColors(ascending = false)
            vm.setOrderType(newOrderType = OrderType.Descending)
            val currentVideoOrder = vm.getVideoOrder()
            vm.setVideoOrder(currentVideoOrder)
        }

        sortByDialogBinding.cardViewAlphabet.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(WordOrder.Alphabet(currentOrderType))
        }
        sortByDialogBinding.cardViewDate.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(WordOrder.Date(currentOrderType))
        }
        sortByDialogBinding.cardViewVideo.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(WordOrder.Video(currentOrderType))
        }

        sortByDialogBinding.clearButton.setOnClickListener {
            vm.setVideoOrder(WordListViewModel.DEFAULT_SORT_MODE)
        }
        sortByDialogBinding.applyButton.setOnClickListener {
            vm.updateVideoList()
            sortByDialog.dismiss()
        }

        vm.wordOrder.observe(videoListActivity) { sortMode ->
            sortByDialogBinding.alphabetCheckBox.isChecked = sortMode is WordOrder.Alphabet
            sortByDialogBinding.dateCheckBox.isChecked = sortMode is WordOrder.Date
            sortByDialogBinding.videoCheckBox.isChecked = sortMode is WordOrder.Video
        }

        sortByDialog.show()
        sortByDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        sortByDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        sortByDialog.window?.setGravity(Gravity.BOTTOM)
    }




    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(videoListActivity)
        fragmentWordListBinding.wordList.layoutManager = layoutManager
        fragmentWordListBinding.wordList.adapter = adapter
        //fragmentWordListBinding.wordList.setHasFixedSize(true)
        val itemDecoration = WordListAdapter.RecyclerViewItemDecoration(16)
        fragmentWordListBinding.wordList.addItemDecoration(itemDecoration)
        adapter.setListener(this, this)
        (fragmentWordListBinding.wordList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }




    private fun openEditMenu() {
        val editMenu = Dialog(videoListActivity)
        editMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = dialogWordListMenuEditBinding.root
        val parentView = dialogView.parent as? ViewGroup
        parentView?.removeView(dialogView)
        editMenu.setContentView(dialogView)

        dialogWordListMenuEditBinding.word.setText(vm.editableWord?.word)
        dialogWordListMenuEditBinding.translation.setText(vm.editableWord?.translation)


        dialogWordListMenuEditBinding.save.setOnClickListener {
            val word = vm.editableWord
            if (word == null) {
                val newWord = WordTranslation(
                    word = dialogWordListMenuEditBinding.word.text.toString(),
                    translation = dialogWordListMenuEditBinding.translation.text.toString(),
                    nativeLanguage = "ru",
                    learnLanguage = "en",
                    timestamp = Date().time
                )
                vm.editWord(newWord)
            } else {
                word.word = dialogWordListMenuEditBinding.word.text.toString()
                word.translation = dialogWordListMenuEditBinding.translation.text.toString()
                vm.editWord(word)
            }

            adapter.changeMode(isSelectionMode = false)
            editMenu.dismiss()
            vm.editableWord = null
        }

        editMenu.show()
        editMenu.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        editMenu.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

        videoListBinding.fragmentNavigation.visibility = visibleInNormalMode
        fragmentWordListBinding.editListLayout.visibility = visibleInSelectionMode
        fragmentWordListBinding.addWordCard.visibility = visibleInNormalMode
    }


    // Если выделено всё
    override fun onSelectAll() {
        fragmentWordListBinding.deSelectAllMenuText.text = videoListActivity.applicationContext.getString(R.string.deselect_all)
        changeCardVisibility(cardView = fragmentWordListBinding.deleteMenu, isVisible = true)
    }

    // Если было выделено всё, а стало на 1 и более меньше
    override fun onDeselectAll() {
        fragmentWordListBinding.deSelectAllMenuText.text = videoListActivity.applicationContext.getString(R.string.select_all)
    }

    // Если не выделено ничего
    override fun onZeroSelect() {
        fragmentWordListBinding.deSelectAllMenuText.text = videoListActivity.applicationContext.getString(R.string.select_all)

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

    override fun onParentChange(position: Int, isChecked: Boolean) {
        val view = fragmentWordListBinding.wordList[position] as ViewGroup
        val parentCheckBox: CheckBox? = findCheckBox(view)
        parentCheckBox?.isChecked = isChecked
    }

    private fun findCheckBox(viewGroup: ViewGroup): CheckBox? {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is CheckBox) {
                return child
            } else if (child is ViewGroup) {
                val checkBox = findCheckBox(child)
                if (checkBox != null) {
                    return checkBox
                }
            }
        }
        return null
    }

}