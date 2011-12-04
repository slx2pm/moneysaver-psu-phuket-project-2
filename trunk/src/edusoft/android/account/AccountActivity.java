package edusoft.android.account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import edusoft.android.database.DatabaseHelper;
import edusoft.android.main.MoneySaverMain;
import edusoft.android.main.R;
import edusoft.android.reuse.AccountObject;
import edusoft.android.reuse.AccountTypeObject;
import edusoft.android.reuse.BalanceObject;
import edusoft.android.reuse.BankObject;
import edusoft.android.reuse.FixAccountType;
import edusoft.android.reuse.Utility;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AccountActivity extends Activity {
	private ArrayList<HashMap<String, Object>> listview_data;
	private HashMap<String, Object> hm;
	private ListView lv1;
	ListViewLayout listLayout;
	DatabaseHelper dbHelp;
	AccountObject accObj;
	BalanceObject balObj;
	AccountTypeObject acctypeObj;
	BankObject bankObj;
	List accList;

	private static final String ACCOUNTIDKEY = "accid";
	private static final String IMGKEY = "image";
	private static final String BANKIDKEY = "bankname";
	private static final String ACCOUNTNUMBERKEY =  "accountnumber";
	private static final String ACCOUNTNAMEKEY =  "accountname";
	private static final String CATEGORYIDKEY = "category";
	private static final String CURRENTBALANCEKEY = "current_balance";
	private static final String INCOMEKEY = "income";
	private static final String LIMITKEY = "limit";
	private static final String EXPENSEKEY = "expense";
	private static final String CURRENTDATEKEY = "date";
	private static final String PERCENTAGEKEY = "percentage";
	private static String curDate = "";
	private static ArrayAdapter adapter;
	private static int totalIncome = 0;
	private static int totalExpense = 0;
	private static int totalRemain = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_main);
		listview_data = new ArrayList<HashMap<String, Object>>();
		lv1 = (ListView) findViewById(R.id.accountMain_accountlist);

		try {

			dbHelp = new DatabaseHelper(this);
			accList = new ArrayList();

			accList = dbHelp.getAccountListData();
			
			totalRemain = totalIncome - totalExpense;
			TextView txt_totalIncome = (TextView) findViewById(R.id.accountMain_totalIncome);
			TextView txt_totalExpense = (TextView) findViewById(R.id.accountMain_totalExpense);
			TextView txt_totalRemain = (TextView) findViewById(R.id.accountMain_totalRemain);

			txt_totalIncome.setText(Integer.toString(totalIncome));
			txt_totalExpense.setText(Integer.toString(totalExpense));
			txt_totalRemain.setText(Integer.toString(totalRemain));
			listLayout = new ListViewLayout(getAllAccount(accList), this);

			lv1.setAdapter(listLayout);
			lv1.setOnItemClickListener(accountEdit);
			Button addAccount = (Button) findViewById(R.id.accountMain_add);
			addAccount.setOnClickListener(buttonAddAccount);
		} catch (Exception e) {
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setMessage(e.toString());
			b.show();
		}
	}
	//======================================= Listenner ================================================
	
	//��÷ӧҹ����͡����� +
	private OnClickListener buttonAddAccount = new OnClickListener() {
		public void onClick(View view) {
			showAccountDialog(false,0, new AccountObject());
		}
	};
	//��÷ӧҹ����ͤ�ԡ������������
	private AdapterView.OnItemClickListener accountEdit = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			try {
				Toast.makeText(getApplicationContext(),"sdfsdf", Toast.LENGTH_LONG).show();
				accObj = new AccountObject();
				accObj.setBankId((String) listview_data.get(position).get(BANKIDKEY));
				accObj.setAccountId((String) listview_data.get(position).get(ACCOUNTIDKEY));
				accObj.setAccountNumber((String) listview_data.get(position).get(ACCOUNTNUMBERKEY));
				accObj.setAccountName((String) listview_data.get(position).get(ACCOUNTNAMEKEY));
				accObj.setAccountTypeId((String) listview_data.get(position).get(CATEGORYIDKEY));
				accObj.setCurrentBalance((String) listview_data.get(position).get(CURRENTBALANCEKEY));
				accObj.setLimitUsage((String) listview_data.get(position).get(LIMITKEY));
				
				final int index = position;
				hm = new HashMap<String, Object>();
				hm = listview_data.get(position);				
				final Dialog dialog = new Dialog(AccountActivity.this);
				dialog.setContentView(R.layout.account_dialog_data);
				dialog.setTitle("�����źѭ��");
				dialog.setCancelable(true);
				
				String accDesc = "�Ţ���ѭ�� :"+ accObj.getAccountNumber()+"\n\n"+
				                 "���ͺѭ�� :" + accObj.getAccountName() + "\n\n"+
				                 "�������ѭ�� :";
				if(accObj.getAccountTypeId().equals(FixAccountType.FIX_CASH_ACCOUNT_ID))
					accDesc +=(FixAccountType.FIX_CASH_ACCOUNT_DESCRIPTION+"\n\n");
				else if(accObj.getAccountTypeId().equals(FixAccountType.FIX_CURRENT_ACCOUNT_ID))
					accDesc +=(FixAccountType.FIX_CURRENT_ACCOUNT_DESCRIPTION+"\n\n");
				else if(accObj.getAccountTypeId().equals(FixAccountType.FIX_SAVING_ACCOUNT_ID))
					accDesc +=(FixAccountType.FIX_SAVING_ACCOUNT_DESCRIPTION+"\n\n");
				else if(accObj.getAccountTypeId().equals(FixAccountType.FIX_DEPOSIT_ACCOUNT_ID))
					accDesc +=(FixAccountType.FIX_DEPOSIT_ACCOUNT_DESCRIPTION+"\n\n");
				
				accDesc = accDesc +"�ʹ�Թ������� :"+ accObj.getCurrentBalance()+"\n\n"+
						  "ǧ�Թ�ӡѴ :"+accObj.getLimitUsage();
							
				TextView accountDescription = (TextView) dialog.findViewById(R.id.account_dialog_data_view);
				accountDescription.setText(accDesc);
				
				Button SubmitButton = (Button) dialog.findViewById(R.id.account_dialog_data_okButton);
				SubmitButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});				
				dialog.show();
			} catch (Exception e) {
				AlertDialog.Builder b = new AlertDialog.Builder(AccountActivity.this);
				b.setMessage(e.toString());
				b.show();
			}
		}
	};
	
	//==================================== Method ��������� Reuse ���� Class AccountActivity =============================
	
	//�֧������ account ����շ����� �������ʴ�
	public ArrayList<HashMap<String, Object>> getAllAccount(List activityList){
		for (int i = 0; i < accList.size(); i++) {
			hm = new HashMap<String, Object>();
			accObj = new AccountObject();
			accObj = (AccountObject) accList.get(i);
			
			hm.put(BANKIDKEY,accObj.getBankId()); //dbHelp.getBankObjectByBankId(accObj.getBankId()).getBankAcronym());
			hm.put(ACCOUNTIDKEY, accObj.getAccountId());	
			hm.put(ACCOUNTNUMBERKEY, accObj.getAccountNumber());
			hm.put(ACCOUNTNAMEKEY, accObj.getAccountName());
			hm.put(IMGKEY, getResources().getIdentifier(dbHelp.getBankObjectByBankId(accObj.getBankId()).getBankAcronym(), "drawable",getPackageName()));
			hm.put(CATEGORYIDKEY, accObj.getAccountTypeId());//dbHelp.getAccTypeByAccountTypeId(accObj.getAccountTypeId()).getAccountType());
			hm.put(CURRENTBALANCEKEY,accObj.getCurrentBalance());
			hm.put(CURRENTDATEKEY, new Utility().getCurrentDate());
			hm.put(INCOMEKEY, new Utility().addDecimal(accObj.getCurrentBalance()));
			hm.put(LIMITKEY, new Utility().addDecimal(accObj.getLimitUsage()));
			
			//�֧��¡�è��·������ͧ �ѭ�չ�� � ��
			String expense = new Utility().addDecimal(dbHelp.getAmountExpenseInCurrentMonth(new Utility().getCurrentMonth(),accObj.getAccountId()));
			
			hm.put(EXPENSEKEY, expense);
			
			
			//ǧ�Թ�ӡѴ�ͧ�ѭ�չ�� � 
			String limitUsage = new Utility().addDecimal(accObj.getLimitUsage());
			if (calculatePercentage(limitUsage, expense) == 100)
				hm.put(PERCENTAGEKEY, calculatePercentage(new Utility().addDecimal(accObj.getLimitUsage()), expense));
			else
				hm.put(PERCENTAGEKEY, 100 - calculatePercentage(new Utility().addDecimal(accObj.getLimitUsage()), expense));				
			listview_data.add(hm);
			/*totalIncome += Integer.parseInt("10000");
			totalExpense = Integer.parseInt("5000")*/;
		}
		return listview_data;
	}
	/* �ʴ�˹�ҵ�ҧ �����ѭ�� ���� ��䢢����źѭ�� �� isEdit ���繵�ǵ�Ǩ�ͺ��� �Ҩҡ������ ���� �����Ԩ���� 
	 * ��� isEdit = true ����� ��÷ӧҹ�Ҩҡ ��ԡ������� listView
	 * ��� isEdit = false ����� ��÷ӧҹ�Ҩҡ ��ԡ���� + ���������ѭ��
	 */
	private void showAccountDialog(Boolean isEdit,int position, AccountObject accounObject) {
		try {			
			final int row = position;
			accObj = accounObject;
			final boolean canEdit = isEdit;
			final Dialog dialog = new Dialog(AccountActivity.this);
			dialog.setContentView(R.layout.account_dialog_add);
			
			dialog.setCancelable(true);
			
			final Spinner bankNameSpinner = (Spinner) dialog.findViewById(R.id.accountDialog_SpinnerBankName);			
			adapter = new ArrayAdapter(AccountActivity.this,android.R.layout.simple_spinner_item, dbHelp.getBankNameArray());
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			bankNameSpinner.setAdapter(adapter);
			
			final EditText accountDialog_EditTextAccountNumber = (EditText) dialog.findViewById(R.id.accountDialog_EditTextAccountNumber);
			final EditText accountDialog_EditTextAccountName = (EditText) dialog.findViewById(R.id.accountDialog_EditTextAccountName);	
			
			final Spinner categorySpinner = (Spinner) dialog.findViewById(R.id.accountDialog_SpinnerCategory);
			adapter = new ArrayAdapter(AccountActivity.this,android.R.layout.simple_spinner_item, dbHelp.getAccTypeArray());
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			categorySpinner.setAdapter(adapter);			
			
			final EditText accountDialog_EditTextAmount = (EditText) dialog.findViewById(R.id.accountDialog_EditTextAmount);				
			final EditText accountDialog_EditTextLimited = (EditText) dialog.findViewById(R.id.accountDialog_EditTextLimited);
			
			if(canEdit){
				
				dialog.setTitle("��䢺ѭ��");
				bankNameSpinner.setSelection(Integer.parseInt(accObj.getBankId())-1);
				accountDialog_EditTextAccountNumber.setText(accObj.getAccountNumber());
				accountDialog_EditTextAccountName.setText(accObj.getAccountName());
				categorySpinner.setSelection(Integer.parseInt(accObj.getAccountTypeId())-1);
				accountDialog_EditTextAmount.setText(new Utility().addDecimal(accObj.getCurrentBalance()));
				accountDialog_EditTextLimited.setText(new Utility().addDecimal(accObj.getLimitUsage()));			
			}else
				dialog.setTitle("�����ѭ��");
			
			Button SubmitButton = (Button) dialog.findViewById(R.id.accountDialog_ButtonSubmit);
			SubmitButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try{
						
						curDate =new Utility().getCurrentDate();						
						hm = new HashMap<String, Object>();
						
						accObj.setBankId(Integer.toString(bankNameSpinner.getSelectedItemPosition()+1));
						accObj.setAccountId(accObj.getAccountId());
						accObj.setAccountNumber(accountDialog_EditTextAccountNumber.getText().toString());
						accObj.setAccountName(accountDialog_EditTextAccountName.getText().toString());
						accObj.setAccountTypeId(Integer.toString(categorySpinner.getSelectedItemPosition()+1));
						accObj.setCurrentBalance(accountDialog_EditTextAmount.getText().toString());
						accObj.setLimitUsage(accountDialog_EditTextLimited.getText().toString());
						
						hm.put(BANKIDKEY, accObj.getBankId());//bankAcronym);
						hm.put(ACCOUNTNUMBERKEY, accObj.getAccountNumber());
						hm.put(ACCOUNTNAMEKEY, accObj.getAccountName());
						hm.put(IMGKEY, getResources().getIdentifier(dbHelp.getBankObjectByBankId(accObj.getBankId()).getBankAcronym(), "drawable", getPackageName()));
						hm.put(CATEGORYIDKEY, accObj.getAccountTypeId());//dbHelp.getAccTypeByAccountTypeId(accObj.getAccountTypeId()).getAccountType());
						hm.put(CURRENTBALANCEKEY,accObj.getCurrentBalance());
						hm.put(CURRENTDATEKEY, new Utility().getCurrentDate());
						hm.put(INCOMEKEY, new Utility().addDecimal(accObj.getCurrentBalance()));
						hm.put(LIMITKEY, new Utility().addDecimal(accObj.getLimitUsage()));
						hm.put(EXPENSEKEY, new Utility().addDecimal("0.00"));
						if (calculatePercentage("0.00", "0.00") == 100)
							hm.put(PERCENTAGEKEY, calculatePercentage("0.00","0.00"));
						else
							hm.put(PERCENTAGEKEY, 100.00 - calculatePercentage("0.00", "0.00"));
						
						if(canEdit){							
							dbHelp.editLimitedUsageForAccount(accObj);
							hm.put(ACCOUNTIDKEY, accObj.getAccountId());							
							listview_data.set(row, hm);
						}else{
							hm.put(ACCOUNTIDKEY, dbHelp.addAccount(accObj));
							listview_data.add(hm);
						}											
						listLayout.notifyDataSetChanged();
						dialog.dismiss();
					}catch(Exception e) {
						AlertDialog.Builder b = new AlertDialog.Builder(AccountActivity.this);
						b.setMessage(e.toString());
						b.show();
					}
				}
			});
			Button CancelButton = (Button) dialog.findViewById(R.id.accountDialog_ButtonCancel);
			CancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			dialog.show();
		} catch (Exception e) {
			AlertDialog.Builder b = new AlertDialog.Builder(AccountActivity.this);
			b.setMessage(e.toString());
			b.show();
		}
	}
	/*  Method �����㹡�äӹǹ % ������� ��º�Ѻ ǧ�Թ���ӡѴ
	 *  �¤Դ�ҡ �Թ������������Դ�� % �ͧ ǧ�Թ�ӡѴ  = �Ҥҷ����� *100/ǧ�Թ���ӡѴ
	 *  ��. ����Դ % �� 0 ���ѧ���� �� �����¶֧��� ������Թǧ�Թ�����ҨӡѴ������� ���ѧ����Թǧ�Թ����շ���������кѭ��
	 */
	public int calculatePercentage(String limit, String expense) {
		int percent = 100;
		if (!expense.equals("0.00"))
			percent = (int) (Double.parseDouble(expense) * 100 / Double.parseDouble(limit));
		return percent;
	}

	//=================================Class ���Ӣ����ŷ������ hm ����� Listview ��������ʴ���˹�Ҩ���ѡ� Tab Activity=================================================
	
	private class ListViewLayout extends BaseAdapter {

		private ArrayList<HashMap<String, Object>> listview_data;
		private LayoutInflater mInflater;
		private int[] colors = new int[] { 0x30ffffff, 0x30808080 };

		public ListViewLayout(ArrayList<HashMap<String, Object>> listview_data,
				Context context) {
			this.listview_data = listview_data;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return listview_data.size();
		}

		@Override
		public Object getItem(int position) {
			return listview_data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				ViewHolder holder;
				final int row = position;
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.listview_data_layout, null);
					holder = new ViewHolder();
					holder.icon = (ImageView) convertView.findViewById(R.id.listDataLayout_bankIcon);
					holder.percentText = (TextView) convertView.findViewById(R.id.listDataLayout_percentText);
					holder.bankName = (TextView) convertView.findViewById(R.id.listDataLayout_bankName);
					holder.category = (TextView) convertView.findViewById(R.id.listDataLayout_category);
					holder.currentDate = (TextView) convertView.findViewById(R.id.listDataLayout_currentDate);
					holder.expenseAmount = (TextView) convertView.findViewById(R.id.listDataLayout_expenseAmount);
					holder.IncomeAmount = (TextView) convertView.findViewById(R.id.listDataLayout_IncomeAmount);
					holder.limit = (TextView) convertView.findViewById(R.id.listDataLayout_Limit);
					holder.edit = (Button) convertView.findViewById(R.id.listDataLayout_ButtonEdit);
					holder.delete = (Button) convertView.findViewById(R.id.listDataLayout_ButtonDelete);					
					holder.percentage = (ProgressBar) convertView.findViewById(R.id.listDataLayout_percentage);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				// Bind the data with the holder.
				holder.icon.setImageResource((Integer) listview_data.get(position).get(IMGKEY));
				holder.percentText.setText(Integer.toString((Integer) listview_data.get(position).get(PERCENTAGEKEY))+ "%");
				holder.bankName.setText(dbHelp.getBankObjectByBankId(((String) listview_data.get(position).get(BANKIDKEY))).getBankAcronym());
				holder.category.setText(dbHelp.getAccTypeByAccountTypeId(((String) listview_data.get(position).get(CATEGORYIDKEY))).getAccountType());
				holder.currentDate.setText((String) listview_data.get(position).get(CURRENTDATEKEY));
				holder.expenseAmount.setText((String) listview_data.get(position).get(EXPENSEKEY));
				holder.IncomeAmount.setText((String) listview_data.get(position).get(INCOMEKEY));
				holder.limit.setText((String) listview_data.get(position).get(LIMITKEY));
				holder.percentage.setProgress(((Integer) listview_data.get(position).get(PERCENTAGEKEY)));
				//���� �Թ�� ������㹡����䢺ѭ��
				holder.edit.setFocusableInTouchMode(false);
				holder.edit.setFocusable(false);
				holder.edit.setTag(position);
				holder.edit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							accObj = new AccountObject();
							accObj.setBankId((String) listview_data.get(row).get(BANKIDKEY));
							accObj.setAccountId((String) listview_data.get(row).get(ACCOUNTIDKEY));
							accObj.setAccountNumber((String) listview_data.get(row).get(ACCOUNTNUMBERKEY));
							accObj.setAccountName((String) listview_data.get(row).get(ACCOUNTNAMEKEY));
							accObj.setAccountTypeId((String) listview_data.get(row).get(CATEGORYIDKEY));
							accObj.setCurrentBalance((String) listview_data.get(row).get(CURRENTBALANCEKEY));
							accObj.setLimitUsage((String) listview_data.get(row).get(LIMITKEY));
							if(row > 0){					
								showAccountDialog(true,row, accObj);
							}else{
								final int index = row;
								hm = new HashMap<String, Object>();
								hm = listview_data.get(row);				
								int bank_image = (Integer) listview_data.get(row).get(IMGKEY);
								final Dialog dialog = new Dialog(AccountActivity.this);
								dialog.setContentView(R.layout.account_dialog_edit);
								dialog.setTitle("���ǧ�Թ");
								dialog.setCancelable(true);
				
								ImageView bankImage = (ImageView) dialog.findViewById(R.id.account_dialog_edit_bankimg);
								bankImage.setImageResource(bank_image);
								TextView bankName = (TextView) dialog.findViewById(R.id.account_dialog_edit_bankname);
								bankName.setText(dbHelp.getBankObjectByBankId(accObj.getBankId()).getBankAcronym());
								final EditText limitEdit = (EditText) dialog.findViewById(R.id.account_dialog_edit_limit);
								limitEdit.setText(accObj.getLimitUsage());
								Button SubmitButton = (Button) dialog.findViewById(R.id.account_dialog_edit_ButtonSubmit);
								SubmitButton.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										try {
											dbHelp.editLimitedUsageForCash(Integer.parseInt((String) hm.get(ACCOUNTIDKEY)),Double.parseDouble(limitEdit.getText().toString()));					
											hm.put(LIMITKEY, new Utility().addDecimal(limitEdit.getText().toString()));
											listview_data.set(index, hm);
											listLayout.notifyDataSetChanged();
											dialog.dismiss();
										} catch (Exception e) {
											AlertDialog.Builder b = new AlertDialog.Builder(AccountActivity.this);
											b.setMessage(e.toString());
											b.show();
										}
									}
								});
								Button CancelButton = (Button) dialog.findViewById(R.id.account_dialog_edit_ButtonCancel);
								CancelButton.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										dialog.cancel();
									}
								});
								dialog.show();
							}
						} catch (Exception e) {
							AlertDialog.Builder b = new AlertDialog.Builder(AccountActivity.this);
							b.setMessage(e.toString());
							b.show();
						}
					}
				});
				
				//���� �ҡ�ҷ ������㹡��ź�ѭ��
				holder.delete.setFocusableInTouchMode(false);
				holder.delete.setFocusable(false);
				holder.delete.setTag(position);
				holder.delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//��Ǩ�ͺ����繡��ź�ѭ�շ��������Թʴ�������
						if(row > 0){
							AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
							builder.setMessage(
							"�س��ͧ���ź�ѭ�� "
							+ dbHelp.getBankObjectByBankId((String)listview_data.get((Integer) v.getTag()).get(BANKIDKEY)).getBankAcronym() + " �������?").setCancelable(false)
							.setPositiveButton("��ŧ",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										hm = new HashMap<String, Object>();
										hm = listview_data.get(row);
										dbHelp.deleteAccount((String) hm.get(ACCOUNTIDKEY));
										listview_data.remove(row);
										listLayout.notifyDataSetChanged();
										dialog.dismiss();
									}
								}).setNegativeButton("¡��ԡ",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
									}
								});
							AlertDialog alert = builder.create();
							alert.show();
						}else
							Toast.makeText(getApplicationContext(),"�������öź�Թʴ�͡�ҡ�к���", Toast.LENGTH_LONG).show();
					}
				});
				//��Ѻ�մ� ��� ������
				int colorPos = position % colors.length;
				convertView.setBackgroundColor(colors[colorPos]);

			} catch (Exception e) {
				AlertDialog.Builder b = new AlertDialog.Builder(AccountActivity.this);
				b.setMessage(e.toString());
				b.show();
			}
			return convertView;
		}

		class ViewHolder {
			ImageView icon;
			TextView percentText;
			TextView bankName;
			TextView category;
			TextView currentDate;
			TextView expenseAmount;
			TextView IncomeAmount;
			TextView limit;
			Button edit;
			Button delete;
			ProgressBar percentage;
		}

	}
}
