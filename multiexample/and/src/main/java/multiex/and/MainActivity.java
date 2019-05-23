package multiex.and;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_layout);
		final EditText text = (EditText) findViewById(R.id.editText1);
		text.setText("Hello world");

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(view -> text.setText("You clicked!"));
	}
}
