package uz.itschool.testapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import uz.itschool.testapp.model.QuestionData
import uz.itschool.testapp.ui.theme.TestAppTheme

// firebase:https://console.firebase.google.com/project/testapp-dd0f1/settings/iam
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingPreview()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestAppTheme {

        AddTest()

        Surface(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)){

            var testList = remember {
                mutableListOf<QuestionData>()
            }
            val selectedTests = remember {
                mutableListOf<QuestionData>()
            }

            var currentTestIndex = remember {
                mutableStateOf(0)
            }

             val testsReference = Firebase.database.reference.child("tests")

            testsReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var tests = mutableListOf<QuestionData>()
                    for(testSnapshot in snapshot.children){
                        val test = testSnapshot.getValue(QuestionData::class.java)
                        test?.let { tests.add(it) }

                    }
                   testList = tests
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "${error.message}")
                }
            })


           testList.shuffle()
            val test = if (testList.isNotEmpty()) testList[currentTestIndex.value] else null

                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Text(
                        text = test?.question.toString(),
                        modifier = Modifier.padding(20.dp, 50.dp),
                        fontSize = 23.sp
                    )
                    Column(){
                        Row{
                            RadioButton(selected = false, onClick = { /*TODO*/ })
                            Text(test?.option1.toString(), fontSize = 20.sp, modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp))}

                        Row{
                            RadioButton(selected = false, onClick = { /*TODO*/ })
                            Text(test?.option2.toString(), fontSize = 20.sp, modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp))}

                        Row{
                            RadioButton(selected = false, onClick = { /*TODO*/ })
                            Text(test?.option3.toString(), fontSize = 20.sp, modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp))}

                        Row{
                            RadioButton(selected = false, onClick = { /*TODO*/ })
                            Text(test?.option4.toString(), fontSize = 20.sp, modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp))}
                    }
                    Button(onClick = {
                        if (testList.isNotEmpty()) {
                            currentTestIndex.value++
                            if (currentTestIndex.value < testList.size) {
                                selectedTests.add(testList[currentTestIndex.value])
                            }
                        }
                    }) {
                        Text(text = if (currentTestIndex.value < testList.size - 1) "Next" else "Finish", fontSize = 20.sp)
                    }
                }

       }
    }
}

@Composable
fun AddTest(){
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("tests")

    val test1 = QuestionData(question = "What is the capital of Uzbekistan?", option1 = "Tashkent", option2 = "Navoi", option3 = "Samarqand", option4 = "Pop")
    reference.child("test1").setValue(test1)

    val test2 = QuestionData(question = "What is the capital of England?", option1 = "Tashkent", option2 = "London", option3 = "Samarqand", option4 = "Pop")
    reference.child("test2").setValue(test2)

    val test3 = QuestionData(question = "What is the capital of Germany?", option1 = "Berlin", option2 = "Navoi", option3 = "Samarqand", option4 = "Pop")
    reference.child("test3").setValue(test3)

    val test4 = QuestionData(question = "What is the capital of Russia?", option1 = "Tashkent", option2 = "Navoi", option3 = "Moscow", option4 = "Pop")
    reference.child("test4").setValue(test4)

    val test5 = QuestionData(question = "What is the capital of Turkiye?", option1 = "Tashkent", option2 = "Navoi", option3 = "Smamarqand", option4 = "Ankara")
    reference.child("test5").setValue(test5)
}