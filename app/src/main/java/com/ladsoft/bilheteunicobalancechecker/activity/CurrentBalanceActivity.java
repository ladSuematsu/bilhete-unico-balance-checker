package com.ladsoft.bilheteunicobalancechecker.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ladsoft.bilheteunicobalancechecker.R;
import com.ladsoft.bilheteunicobalancechecker.databinding.ActivityCurrentBalanceBinding;
import com.ladsoft.bilheteunicobalancechecker.model.BilheteUnicoInfo;
import com.ladsoft.bilheteunicobalancechecker.presenter.BalancePresenter;
import com.ladsoft.bilheteunicobalancechecker.presenter.CurrentBalancePresenter;
import com.ladsoft.bilheteunicobalancechecker.task.TaskCallback;
import com.ladsoft.view.text.Mask;

public class CurrentBalanceActivity extends AppCompatActivity {

    ActivityCurrentBalanceBinding binding;
    private BalancePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_balance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_balance);
        presenter = new CurrentBalancePresenter(new Handler(), callback);

        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_current_balance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        binding.contentCurrentBalance.cardId.setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.contentCurrentBalance.cardId.addTextChangedListener(Mask.get("##.##.########-#", binding.contentCurrentBalance.cardId));

        binding.contentCurrentBalance.birthDate.setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.contentCurrentBalance.birthDate.addTextChangedListener(Mask.get("##/##/####", binding.contentCurrentBalance.birthDate));

        binding.contentCurrentBalance.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getCurrentBalance(binding.contentCurrentBalance.cardId.getText().toString(),
                        binding.contentCurrentBalance.birthDate.getText().toString());
            }
        });
    }

    private TaskCallback callback = new TaskCallback() {
        @Override
        public void onBalanceResponse(BilheteUnicoInfo info) {
            Toast.makeText(getBaseContext(), String.valueOf(info.getCommonPassBalance()), Toast.LENGTH_LONG).show();
        }
    };
}
