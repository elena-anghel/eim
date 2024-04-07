package com.example.contactsmanager

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: ContactsViewModel by viewModels()

        setContent {
            ContactsManagerUI(viewModel = viewModel)
        }
    }
}

@Composable
fun ContactsManagerUI(viewModel: ContactsViewModel) {
    val context = LocalContext.current

    /*
     Create a launcher for the ActivityResultContract
     more info here: https://developer.android.com/training/basics/intents/result
     */

    val saveContactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(
                context,
                "Contact saved successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { viewModel.showAdditionalFields = !viewModel.showAdditionalFields }) {
            Text(text = if (viewModel.showAdditionalFields) stringResource(id = R.string.hide_additional_fields) else stringResource(id = R.string.show_additional_fields))
        }

        CustomOutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = stringResource(id = R.string.name)
        )

        CustomOutlinedTextField(
            value = viewModel.phone,
            onValueChange = { viewModel.phone = it },
            label = stringResource(id = R.string.phone_number)
        )

        CustomOutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = stringResource(id = R.string.email)
        )

        CustomOutlinedTextField(
            value = viewModel.address,
            onValueChange = { viewModel.address = it },
            label = stringResource(id = R.string.address)
        )

        if (viewModel.showAdditionalFields) {
            CustomOutlinedTextField(
                value = viewModel.jobTitle,
                onValueChange = { viewModel.jobTitle = it },
                label = stringResource(id = R.string.job_title)
            )

            CustomOutlinedTextField(
                value = viewModel.company,
                onValueChange = { viewModel.company = it },
                label = stringResource(id = R.string.company)
            )

            CustomOutlinedTextField(
                value = viewModel.website,
                onValueChange = { viewModel.website = it },
                label = stringResource(id = R.string.website)
            )

            CustomOutlinedTextField(
                value = viewModel.im,
                onValueChange = { viewModel.im = it },
                label = stringResource(id = R.string.IM)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                viewModel.saveContact(
                    name = viewModel.name,
                    phone = viewModel.phone,
                    email = viewModel.email,
                    address = viewModel.address,
                    jobTitle = viewModel.jobTitle,
                    company = viewModel.company,
                    website = viewModel.website,
                    im = viewModel.im,
                    launcher = saveContactLauncher
                )
            }) {
                Text(stringResource(id = R.string.save))
            }
            Button(onClick = { viewModel.clearFields() }) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    }
}

/*
    Create a custom OutlinedTextField that hides the keyboard when the user clicks the Done button
    on the keyboard
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )
    )
}
