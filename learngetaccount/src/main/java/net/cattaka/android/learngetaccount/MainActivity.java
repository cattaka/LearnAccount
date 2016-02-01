package net.cattaka.android.learngetaccount;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static final String ACCOUNT_TYPE = "net.cattaka.android.learnaccount";

    static Handler sHandler = new Handler();
    ListView mAccountsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountsList = (ListView) findViewById(R.id.list_accounts);
        mAccountsList.setOnItemClickListener(this);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountManager am = AccountManager.get(MainActivity.this);
                AuthenticatorDescription[] ads = am.getAuthenticatorTypes();
                List<Account> accounts = new ArrayList<Account>();
                for (AuthenticatorDescription ad : ads) {
                    accounts.addAll(Arrays.asList(am.getAccountsByType(ad.type)));
                }
                mAccountsList.setAdapter(new ArrayAdapter<Account>(MainActivity.this, android.R.layout.simple_list_item_1, accounts));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView == mAccountsList) {
            final Account account = (Account) adapterView.getItemAtPosition(position);
            final AccountManager am = AccountManager.get(this);
            AccountManagerFuture<Bundle> amf = am.getAuthToken(account, account.type, null, this, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    Bundle bundle = null;
                    try {
                        bundle = future.getResult();
                        String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                        String accountType = bundle.getString(AccountManager.KEY_ACCOUNT_TYPE);
                        String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                        String password = am.getPassword(account);
                        String userData = am.getUserData(account, "test");
                        Toast.makeText(MainActivity.this, String.valueOf(bundle) + ":" + password + ":" + userData, Toast.LENGTH_SHORT).show();
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

}
