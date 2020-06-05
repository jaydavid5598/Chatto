package com.david.myapplication.note.list

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.david.myapplication.R
import com.david.myapplication.note.data_model.NoteRequest
import com.david.myapplication.note.write.WriteNoteFragment
import com.david.myapplication.note.write.view.WriteNoteFragmentVh
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_list_note_fragment.*

class ListNoteFragment : Fragment(){
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_list_note_fragment, container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Notes"
        fetchNotes()
    }

    private fun fetchNotes() {
        val currentUser = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/notes/$currentUser")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val note = p0.getValue(NoteRequest::class.java)?:return
                adapter.add(WriteNoteFragmentVh(note))
                recyclerview_notes.adapter = adapter
                recyclerview_notes.scrollToPosition(adapter.itemCount -1)
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(p0: DatabaseError) {
               d("Event",p0.details)
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                d("Event",p0.value.toString())
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                d("Event",p0.value.toString())
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                d("Event",p0.value.toString())
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_note_btn.setOnClickListener {
            startActivity(Intent(activity,WriteNoteFragment::class.java))
        }
    }
}