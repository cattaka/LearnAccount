package net.cattaka.android.learnaccount;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //    static final String ACCOUNT_TYPE = "net.cattaka.android.learnaccount";
    static final String ACCOUNT_TYPE = "com.facebook.katana";

    static Handler sHandler = new Handler();
    ListView mAccountsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAccountsList = (ListView) findViewById(R.id.list_accounts);
        mAccountsList.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountManager am = AccountManager.get(MainActivity.this);
                Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
                mAccountsList.setAdapter(new ArrayAdapter<Account>(MainActivity.this, android.R.layout.simple_list_item_1, accounts));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView == mAccountsList) {
            final Account account = (Account) adapterView.getItemAtPosition(position);
            final AccountManager am = AccountManager.get(this);
            am.getAuthToken(account, ACCOUNT_TYPE, null, this, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    Bundle bundle = null;
                    try {
                        bundle = future.getResult();
                        String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                        String accountType = bundle.getString(AccountManager.KEY_ACCOUNT_TYPE);
                        String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                        String password = am.getPassword(account);
                        Toast.makeText(MainActivity.this, accountName + ":" + accountType + ":" + authToken + ":" + password, Toast.LENGTH_SHORT).show();
                    } catch (OperationCanceledException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (AuthenticatorException e) {
                        e.printStackTrace();
                    }
                }
            }, sHandler);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
