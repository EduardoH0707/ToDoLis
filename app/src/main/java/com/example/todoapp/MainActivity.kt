import android.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class MainActivity : AppCompatActivity() {
    private var db: AppDatabase? = null
    private var adapter: TaskAdapter? = null
    private var taskList: MutableList<Task>? = null
    private var titleEditText: EditText? = null
    private var descriptionEditText: EditText? = null
    private var addButton: Button? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Inicializando componentes
        titleEditText = findViewById<EditText>(R.id.titleEditText)
        descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        addButton = findViewById<Button>(R.id.addButton)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        // Inicializando banco de dados
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "task-db")
            .allowMainThreadQueries()
            .build()

        // Carregar tarefas
        taskList = db!!.taskDao().getAllTasks()


        // Configurar RecyclerView
        adapter = TaskAdapter(taskList, OnDeleteClickListener { task: Task? ->
            db!!.taskDao().delete(task)
            refreshTaskList()
        })
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setAdapter(adapter)

        // Botão de adicionar
        addButton.setOnClickListener(View.OnClickListener { v: View? ->
            val title = titleEditText.getText().toString()
            val description = descriptionEditText.getText().toString()
            if (!title.isEmpty() && !description.isEmpty()) {
                db!!.taskDao().insert(Task(title, description))
                titleEditText.setText("")
                descriptionEditText.setText("")
                refreshTaskList()
            }
        })
    }

    private fun refreshTaskList() {
        taskList!!.clear()
        taskList!!.addAll(db!!.taskDao().getAllTasks())
        adapter!!.notifyDataSetChanged()
    }
}