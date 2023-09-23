package com.example.roompractice.view

import SoccerAdapter
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roompractice.MainActivity
import com.example.roompractice.R
import com.example.roompractice.databinding.FragmentHomeBinding
import com.example.roompractice.db.SoccerEntity
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainActivity: MainActivity

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SoccerAdapter

    // 선택된 항목 수와 삭제 버튼
    private lateinit var selectedItemCount: TextView
    private lateinit var deleteButton: Button
    private lateinit var editButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = context as MainActivity
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.soccerPlayerRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = SoccerAdapter()
        recyclerView.adapter = adapter

        adapter.setOnItemSelectedListener(object : SoccerAdapter.OnItemSelectedListener {
            override fun onItemSelected(selectedCount: Int) {
                // 선택된 항목 수 업데이트
                selectedItemCount.text = resources.getString(R.string.selected_items, adapter.selectedItems.size)
                selectedItemCount.visibility = if (selectedCount > 0) View.VISIBLE else View.GONE

                // 삭제 버튼 활성화/비활성화
                deleteButton.visibility = if (selectedCount > 0) View.VISIBLE else View.GONE
                editButton.visibility = if (selectedCount == 1) View.VISIBLE else View.GONE
            }
        })

        selectedItemCount = binding.selectedItemCount
        deleteButton = binding.deleteButton
        deleteButton.setOnClickListener { deleteSelectedItems() }

        editButton = binding.editButton
        editButton.setOnClickListener { showEditDialog() } // editButton에 클릭 리스너 설정

        binding.floatingActionButton.setOnClickListener(this@HomeFragment)

        loadSoccerData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.floatingActionButton -> showPlayerRegistrationDialog()
            R.id.editButton -> showEditDialog()
        }
    }

    private fun showPlayerRegistrationDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_player_registration)

        val editTextName = dialog.findViewById<EditText>(R.id.editTextName)
        val editTextTeam = dialog.findViewById<EditText>(R.id.editTextTeam)
        val editTextNationality = dialog.findViewById<EditText>(R.id.editTextNationality)
        val buttonRegister = dialog.findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val team = editTextTeam.text.toString()
            val nationality = editTextNationality.text.toString()

            val soccerEntity = SoccerEntity(0, name, team, nationality)

            lifecycleScope.launch {
                try {
                    mainActivity.roomDb.soccer().insert(soccerEntity)
                } catch (e: Exception) {
                    // 삽입 실패 처리
                } finally {
                    dialog.dismiss()
                    loadSoccerData()
                }
            }
        }

        dialog.show()
    }

    private fun showEditDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_player_edit) // 수정 다이얼로그 레이아웃을 사용하도록 설정

        val player = adapter.selectedItems.first()

        val editTextName = dialog.findViewById<EditText>(R.id.editTextName)
        val editTextTeam = dialog.findViewById<EditText>(R.id.editTextTeam)
        val editTextNationality = dialog.findViewById<EditText>(R.id.editTextNationality)
        val buttonEdit = dialog.findViewById<Button>(R.id.editButton)

        // 기존 데이터를 EditText에 설정
        editTextName.setText(player.name)
        editTextTeam.setText(player.team)
        editTextNationality.setText(player.nationality)

        buttonEdit.setOnClickListener {
            // 수정된 데이터를 가져오기
            val newName = editTextName.text.toString()
            val newTeam = editTextTeam.text.toString()
            val newNationality = editTextNationality.text.toString()

            // 수정된 데이터를 SoccerEntity로 생성
            val updatedSoccer = SoccerEntity(player.id, newName, newTeam, newNationality)

            // 데이터베이스 업데이트
            updateSoccer(updatedSoccer)

            // 다이얼로그 닫기
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun loadSoccerData() {
        lifecycleScope.launch {
            val soccerData = mainActivity.roomDb.soccer().select()
            adapter.submitList(soccerData)
        }
    }

    private fun deleteSelectedItems() {
        val selectedItems = adapter.getSelectedItems()
        if (selectedItems.isNotEmpty()) {
            for (item in selectedItems) {
                // 데이터베이스에서 삭제
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        mainActivity.roomDb.soccer().delete(item)
                    } catch (e: Exception) {
                        // 삭제 실패 처리
                    } finally {
                        loadSoccerData()
                    }
                }
            }
            // 선택된 항목 초기화
            adapter.clearSelectedItems()
        }
    }

    // 데이터베이스 업데이트 함수
    private fun updateSoccer(soccer: SoccerEntity) {
        // 데이터베이스 업데이트
        lifecycleScope.launch {
            try {
                mainActivity.roomDb.soccer().update(soccer)
            } catch (e: Exception) {
                // 업데이트 실패 처리
                e.printStackTrace()
            } finally {
                // 데이터 업데이트 후 RecyclerView 갱신
                loadSoccerData()
            }
        }
    }
}
