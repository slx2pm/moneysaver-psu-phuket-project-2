package edusoft.android.balance;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import edusoft.android.account.AccountActivity;
import edusoft.android.database.DatabaseHelper;
import edusoft.android.main.MoneySaverMain;
import edusoft.android.main.R;
import edusoft.android.reuse.AccountObject;
import edusoft.android.reuse.BalanceObject;
import edusoft.android.reuse.FixAccountType;
import edusoft.android.reuse.FixTypeUsing;
import edusoft.android.reuse.Utility;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BalanceActivity extends Activity {
	public final int CAMERA_RESULT = 0;
	boolean canEdit ;
	
	BalanceObject balanceObject;
	Bitmap bmp;
	
	private ArrayList<HashMap<String, Object>> listview_data;
	private HashMap<String, Object> hm;
	private ListView lv1;
	private TextView txt_totalIncome;
	private TextView txt_totalExpense;
	private TextView editTextDescription;
	private TextView editTextAmount;
	private TextView txtDate;
	private TextView txtTime;
	private TextView balTitle;
	private TextView balDescription;
	private Button addBalance;
	private Button viewByDate;
	private ImageView imageCam;
	private ImageView imageCamData;
	private Spinner payTypeSpinner;
	private Spinner payUsingWaySpinner;
	private Button buttunCamera;
	private Button buttonCalendar;
	private Button buttonTime;
	private Button submitButton;
	private Button cancelButton;
	
	ListViewLayout listLayout;
	DatabaseHelper dbHelp;
	List activityList ;
	
	private String payTypeList[] = { "����Ѻ", "��¨���", "�͹�Թ", "�͹�Թ" };
	public static int TAKE_IMAGE = 111;
	private static final String ACTIVITYIDKEY = "activityId";
	private static final String ACCOUNTIDKEY = "accountId";
	private static final String IMGKEY = "image";
	private static final String TYPEUSINGKEY = "payType";
	private static final String DESCRIPTIONKEY = "description";
	private static final String DATEKEY = "date";
	private static final String TIMEKEY = "time";
	private static final String AMOUNTKEY = "amount";
	
	private static String curDate = "";
	private static ArrayAdapter adapterPayType;
	private static ArrayAdapter adapterPayUsing;
	private final Calendar c = Calendar.getInstance();
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private static String date = "";
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	static final int DATE_DIALOG_DATA_ID = 2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balance_main);
		
		
		try {
			onCreateInitailLayout();
			listview_data = new ArrayList<HashMap<String, Object>>();
			dbHelp = new DatabaseHelper(this);
			activityList = new ArrayList();
				
			curDate = new Utility().getCurrentDate();
			
			activityList = dbHelp.getActivityListData(curDate);
			
			txt_totalIncome.setText(dbHelp.getIncomeAmountByDate(curDate));
			txt_totalExpense.setText(dbHelp.getExpenseAmountByDate(curDate));
			listLayout = new ListViewLayout(getAllActivity(activityList),this);

			lv1.setAdapter(listLayout);
			lv1.setOnItemClickListener(balanceEdit);		
			
			viewByDate.setOnClickListener(dateDataPicker);
				
			addBalance.setOnClickListener(buttonAddBalance);
			
			mDay = Integer.parseInt(new Utility().getCurrentDay());
			mMonth = Integer.parseInt(new Utility().getCurrentMonth());
			mYear = Integer.parseInt(new Utility().getCurrentYear());
		} catch (Exception e) {
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setMessage(e.toString());
			b.show();
		}
	}
	@Override
    protected void onPause() {
        Log.d("LifeCycleTest", "onPause() called");
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LifeCycleTest", "onResume() called");
    }

	//======================================= initial layout ===========================================
	private void onCreateInitailLayout()
	{
		lv1 = (ListView) findViewById(R.id.balance_main_balancelist);
		txt_totalIncome = (TextView) findViewById(R.id.balance_main_totalIncome);
		txt_totalExpense = (TextView) findViewById(R.id.balance_main_totalExpense);
		viewByDate = (Button) findViewById(R.id.balance_main_calendarView);
		addBalance = (Button) findViewById(R.id.balance_main_add);		
	}
	private void dialogManageDataLayout(Dialog dialog)
	{
		editTextDescription = (EditText) dialog.findViewById(R.id.balance_dialog_add_editTextDescription);
		editTextAmount = (EditText) dialog.findViewById(R.id.balance_dialog_add_editTextAmount);			
		payTypeSpinner = (Spinner) dialog.findViewById(R.id.balance_dialog_add_spinnerUsingType);
		imageCam = (ImageView) dialog.findViewById(R.id.balance_dialog_imgCam);
		buttunCamera =(Button) dialog.findViewById(R.id.balance_dialog_add_ButtonCamera);
		payUsingWaySpinner = (Spinner) dialog.findViewById(R.id.balance_dialog_add_spinnerUsingWay);
		txtDate = (TextView) dialog.findViewById(R.id.balance_dialog_add_textViewDate);
		txtTime = (TextView) dialog.findViewById(R.id.balance_dialog_add_textViewTime);
		buttonCalendar = (Button) dialog.findViewById(R.id.balance_dialog_add_ButtonCalendar);
		txtTime = (TextView) dialog.findViewById(R.id.balance_dialog_add_textViewTime);
		buttonTime = (Button) dialog.findViewById(R.id.balance_dialog_add_ButtonTime);
		submitButton = (Button) dialog.findViewById(R.id.balance_dialog_add_buttonOK);
	}
	private void dialogShowActivityDataLayout(Dialog dialog)
	{
		balTitle = (TextView) dialog.findViewById(R.id.balance_dialog_data_title);
		balDescription = (TextView) dialog.findViewById(R.id.balance_dialog_data_desc);
		imageCamData = (ImageView) dialog.findViewById(R.id.balance_dialog_data_location);
		submitButton = (Button) dialog.findViewById(R.id.balance_dialog_data_okButton);
	}
	//======================================= Listenner ================================================
	//��÷ӧҹ����͡����� +
	private OnClickListener buttonAddBalance = new OnClickListener() {
		public void onClick(View view) {
			showBalanceDialog(false,new BalanceObject());
		}
	};
	//��÷ӧҹ����ͤ�ԡ������������
	private AdapterView.OnItemClickListener balanceEdit = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			try {
				balanceObject = new BalanceObject();
				balanceObject.setActivityId((String) listview_data.get(position).get(ACTIVITYIDKEY));
				balanceObject.setDescription((String) listview_data.get(position).get(DESCRIPTIONKEY));
				balanceObject.setTypeUsing((String) listview_data.get(position).get(TYPEUSINGKEY));						
				balanceObject.setAccountId((String) listview_data.get(position).get(ACCOUNTIDKEY));						
				balanceObject.setDate((String) listview_data.get(position).get(DATEKEY));
				balanceObject.setTime((String) listview_data.get(position).get(TIMEKEY));
				balanceObject.setNetPrice((String) listview_data.get(position).get(AMOUNTKEY));	
												
				final int index = position;
				hm = new HashMap<String, Object>();
				hm = listview_data.get(position);				
				final Dialog dialog = new Dialog(BalanceActivity.this);
				dialog.setContentView(R.layout.balance_dialog_data);
				dialog.setTitle("����������Ѻ-��¨���");
				dialog.setCancelable(true);
				dialogShowActivityDataLayout(dialog);
							
				
				balTitle.setText(balanceObject.getDescription());
				bmp = dbHelp.getActivityImageById(balanceObject.getActivityId(),BitmapFactory.decodeResource(getResources(), R.drawable.no_image));
							
				imageCamData.setImageBitmap(bmp);
					
				String balDesc = "�������������� : ";
				//��������������
				if(balanceObject.getTypeUsing().equals(FixTypeUsing.fixExpense))
					balDesc +=(FixTypeUsing.fixExpenseDescription+"\n\n");
				else if(balanceObject.getTypeUsing().equals(FixTypeUsing.fixIncome))
					balDesc +=(FixTypeUsing.fixIncomeDescription+"\n\n");
				else if(balanceObject.getTypeUsing().equals(FixTypeUsing.fixTransfer))
					balDesc +=(FixTypeUsing.fixTransferDescription+"\n\n");
				else if(balanceObject.getTypeUsing().equals(FixTypeUsing.fixWithdraw))
					balDesc +=(FixTypeUsing.fixWithdrawDescription+"\n\n");
				balDesc += "��ͧ�ҧ�������� : ";
				//�������ѭ��
				if(balanceObject.getAccountId().equals(FixAccountType.FIX_CASH_ACCOUNT_ID))
					balDesc +=(FixAccountType.FIX_CASH_ACCOUNT_DESCRIPTION+"\n\n");
				else if(balanceObject.getAccountId().equals(FixAccountType.FIX_CURRENT_ACCOUNT_ID))
					balDesc +=(FixAccountType.FIX_CURRENT_ACCOUNT_DESCRIPTION+"\n\n");
				else if(balanceObject.getAccountId().equals(FixAccountType.FIX_SAVING_ACCOUNT_ID))
					balDesc +=(FixAccountType.FIX_SAVING_ACCOUNT_DESCRIPTION+"\n\n");
				else if(balanceObject.getAccountId().equals(FixAccountType.FIX_DEPOSIT_ACCOUNT_ID))
					balDesc +=(FixAccountType.FIX_DEPOSIT_ACCOUNT_DESCRIPTION+"\n\n");
		
				balDesc += "�ѹ : "+ balanceObject.getDate()+"\n\n"+
						  "���� : "+balanceObject.getTime()+"\n\n"+
						  "�ӹǹ : "+balanceObject.getNetPrice();
				
				
				balDescription.setText(balDesc);
				
				submitButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) { 
						dialog.dismiss();
						bmp = null;
						balanceObject = null;
					}
				});				
				dialog.show();
			} catch (Exception e) {
				AlertDialog.Builder b = new AlertDialog.Builder(BalanceActivity.this);
				b.setMessage(e.toString());
				b.show();
			}
		}
	};
	//��Ҥ�ԡ �ٻ��ԷԹ�˹����ѡ�ͧ  ����Ѻ-��¨��� ������鹢���������Ѻ-��¨��¢ͧ�ѹ��� � ���ʴ� ������¡ method onCreateDialog
	private OnClickListener dateDataPicker = new OnClickListener() {
		public void onClick(View view) {
			try{					
				showDialog(DATE_DIALOG_DATA_ID);
			} catch (Exception e) {
				AlertDialog.Builder b = new AlertDialog.Builder(BalanceActivity.this);
				b.setMessage(e.toString());
				b.show();
			}		
		}
	};
	//��Ҥ�ԡ �ٻ��ԷԹ ������� ��駤�� �ѹ ��͹ �� ������¡ method onCreateDialog
	private OnClickListener datePicker = new OnClickListener() {
		public void onClick(View view) {
			showDialog(DATE_DIALOG_ID);
		}
	};
	//�ó� ����¹ �ѹ ��͹ �� � dialod date ��Ҷ�ж١ set ����������ʴ�����
	private DatePickerDialog.OnDateSetListener mDateDataSetListener = new DatePickerDialog.OnDateSetListener() {
			// onDateSet method
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				try {
					mYear = year;
					mMonth = monthOfYear;
					mDay = dayOfMonth;
					updateDataByDate();
				} catch (Exception e) {
					AlertDialog.Builder b = new AlertDialog.Builder(BalanceActivity.this);
					b.setMessage(e.toString());
					b.show();
				}
			}
		};	
	//�ó� ����¹ �ѹ ��͹ �� � dialod date ��Ҷ�ж١ set ����������ʴ�����
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			try {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDate();
			} catch (Exception e) {
				AlertDialog.Builder b = new AlertDialog.Builder(BalanceActivity.this);
				b.setMessage(e.toString());
				b.show();
			}
		}
	};	
	//��Ҥ�ԡ�ٻ���ԡ� ������� ��駤�� ���� ������¡ method onCreateDialog
	private OnClickListener timePicker = new OnClickListener() {
			public void onClick(View view) {
				showDialog(TIME_DIALOG_ID);
			}
		};
	//�ó� ����¹ ���� � dialod time ��Ҷ�ж١ set ��� ������ʴ�����
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mHour = hourOfDay;
				mMinute = minute;
				updateTime();
			}
		};
	//��÷ӧҹ���ǹ�ͧ��á����������ٻ
	private OnClickListener takePhoto = new OnClickListener() {
		public void onClick(View view) {
			File imageFile = new File(getImagePath());
			Uri imageFileUri = Uri.fromFile(imageFile);
			Intent i = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					imageFileUri);
			startActivityForResult(i, CAMERA_RESULT);
		}
	};
	//==================================== Method ��������� Reuse ���� Class BalanceActivity =============================	
	// �֧������ activity ������͹�Ѩ�غѹ ������ʴ� �����§�ӴѺ��� �ѹ/��͹/�� ��� ���ҷ��ӡԨ����
	public ArrayList<HashMap<String, Object>> getAllActivity(List activityList){
		for (int i = 0; i < activityList.size(); i++) {
			hm = new HashMap<String, Object>();
			balanceObject = (BalanceObject) activityList.get(i);
			hm.put(ACTIVITYIDKEY, balanceObject.getActivityId());
			hm.put(ACCOUNTIDKEY, balanceObject.getAccountId());				
			hm.put(IMGKEY, getResources().getIdentifier(dbHelp.getBankAcronymByAccountId(balanceObject.getAccountId()), "drawable",getPackageName()));
			hm.put(TYPEUSINGKEY, balanceObject.getTypeUsing());
			hm.put(DESCRIPTIONKEY, balanceObject.getDescription());	
			hm.put(DATEKEY, curDate);
			hm.put(TIMEKEY, balanceObject.getTime());
			hm.put(AMOUNTKEY, balanceObject.getNetPrice());			
			listview_data.add(hm);
			/*if (balanceObject.getNetPrice().indexOf("-") > -1)
				totalExpense += Double.parseDouble(balanceObject.getNetPrice().substring(1));
			else
				totalIncome += Double.parseDouble(balanceObject.getNetPrice().substring(0));*/
		}
		balanceObject = null;
		return listview_data;
	}	
	/* �ʴ�˹�ҵ�ҧ �����Ԩ���� ���� ��䢢����šԨ���� �� isEdit ���繵�ǵ�Ǩ�ͺ��� �Ҩҡ������ ���� �����Ԩ���� 
	 * ��� isEdit = true ����� ��÷ӧҹ�Ҩҡ ��ԡ������� listView
	 * ��� isEdit = false ����� ��÷ӧҹ�Ҩҡ ��ԡ���� + ���������Ԩ����
	 */
	private void showBalanceDialog(Boolean isEdit, BalanceObject balanceObject) {
		try {
			//᷹���������ó� �к��Դ dialog �ѵ���ѵ ��� �����ٻ
			this.balanceObject = balanceObject;
			
			final BalanceObject bal = balanceObject;
			canEdit = isEdit;
			final Dialog dialog = new Dialog(BalanceActivity.this);
			dialog.setContentView(R.layout.balance_dialog_add);
			
			dialog.setCancelable(true);		
			dialogManageDataLayout(dialog);
			//�֧����� layout �ҡ��� xml
			
			adapterPayType = new ArrayAdapter(BalanceActivity.this,android.R.layout.simple_spinner_item, payTypeList);
			adapterPayType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
			buttunCamera.setOnClickListener(takePhoto);

			payTypeSpinner.setAdapter(adapterPayType);		
			
			adapterPayUsing = new ArrayAdapter(BalanceActivity.this,android.R.layout.simple_spinner_item,getBankInAccount() );
			adapterPayUsing.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			payUsingWaySpinner.setAdapter(adapterPayUsing);		
			
			buttonCalendar.setOnClickListener(datePicker);
			
			buttonTime.setOnClickListener(timePicker);			
			
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			mHour = c.get(Calendar.HOUR_OF_DAY);
			mMinute = c.get(Calendar.MINUTE);
			updateDate();
			updateTime();
			//��ҡ��ҡ listview ������繡����� ���ա�� set �������繤�ҡ�͹˹���ѵ��ѵ
			if (canEdit) {
				dialog.setTitle("�������Ѻ-��¨���");
				editTextDescription.setText(bal.getDescription());				
				payTypeSpinner.setSelection(Integer.parseInt(bal.getTypeUsing()));
				payUsingWaySpinner.setSelection(adapterPayUsing.getPosition(dbHelp.getBanknameForSpinner(balanceObject.getAccountId())));
				txtDate.setText("�ѹ��� : "+bal.getDate());
				txtTime.setText("���� : "+bal.getTime());
				editTextAmount.setText(bal.getNetPrice());
				imageCam.setImageBitmap(dbHelp.getActivityImageById(bal.getActivityId(),BitmapFactory.decodeResource(getResources(), R.drawable.no_image)));
				/*try
				{				
					imageCamData.setImageBitmap(dbHelp.getActivityImageById(bal.getActivityId()));
				}
				catch (Exception e) {
					imageCamData.setImageResource(R.drawable.no_image);
				}*/
				
			}else
				dialog.setTitle("��������Ѻ-��¨���");
			
			submitButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try{
						boolean updateTabData = false;
						hm = new HashMap<String, Object>();						
						//�Ѿഷ�ѹ ��������繻Ѩ�غѹ �������ҡ����� OK ����������͡��������е�ͧ������� �Ѩ�غѹ��ҹ��
						mYear = c.get(Calendar.YEAR);
						mMonth = c.get(Calendar.MONTH);
						mDay = c.get(Calendar.DAY_OF_MONTH);
						mHour = c.get(Calendar.HOUR_OF_DAY);
						mMinute = c.get(Calendar.MINUTE);
						if(editTextDescription.getText().toString().trim().length()  >   0   &&
					       editTextAmount.getText().toString().trim().length()  	 >   0     )
						{
							bal.setActivityId(bal.getActivityId());
							bal.setDescription(editTextDescription.getText().toString());
							
							if(bmp != null) 							bal.setBitmap(bmp);											
							
							bal.setTypeUsing(Integer.toString(payTypeSpinner.getSelectedItemPosition()));						
							bal.setAccountId(getAccountIdWithIndex(payUsingWaySpinner.getSelectedItemPosition()));						
							bal.setDate(txtDate.getText().toString().substring(txtDate.getText().toString().indexOf(": ")+2));
							bal.setTime(txtTime.getText().toString().substring(txtTime.getText().toString().indexOf(": ")+2));
							bal.setNetPrice(editTextAmount.getText().toString());	

							if(canEdit)
							{				
								updateTabData = dbHelp.editActivity(bal);
								canEdit = false;
							}else
							{
								//Toast.makeText(getApplicationContext(),dbHelp.getCurrentBalanceWithAccountId(bal.getAccountId()) +","+ bal.getNetPrice(), Toast.LENGTH_LONG).show();
								updateTabData = dbHelp.addActivity(bal);																
							}			
							bmp = null;	
							if(updateTabData){
								//�ӡ�ô֧������������������� �����Ŷ١���§����ѹ-���ҷ��ӡԨ����
								listview_data.clear();							
								listview_data = getAllActivity(dbHelp.getActivityListData(curDate));							
								listLayout.notifyDataSetChanged();
								dialog.dismiss();
							}else{
								Toast.makeText(getApplicationContext(),"�ӹǹ�Թ�ҡ�����Թ������� ��س�����ա����", Toast.LENGTH_LONG).show();
							}			
						}
						else
						{
							Toast.makeText(getApplicationContext(),"��سҡ�͡���������ú��ǹ", Toast.LENGTH_LONG).show();
						}
														
					}catch (Exception e) {
						AlertDialog.Builder b = new AlertDialog.Builder(BalanceActivity.this);
						b.setMessage(e.toString());
						b.show();
					}
				}
			});
			cancelButton = (Button) dialog.findViewById(R.id.balance_dialog_add_buttonCancel);
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//�Ѿഷ�ѹ ��������繻Ѩ�غѹ �������ҡ����� OK ����������͡��������е�ͧ������� �Ѩ�غѹ��ҹ��
					mYear = c.get(Calendar.YEAR);
					mMonth = c.get(Calendar.MONTH);
					mDay = c.get(Calendar.DAY_OF_MONTH);
					mHour = c.get(Calendar.HOUR_OF_DAY);
					mMinute = c.get(Calendar.MINUTE);
					dialog.cancel();
				}
			});
			
			dialog.show();
		} catch (Exception e) {
			AlertDialog.Builder b = new AlertDialog.Builder(BalanceActivity.this);
			b.setMessage(e.toString());
			b.show();

		}
	}	
	//=================================��÷ӧҹ㹡�����¡˹�ҵ�ҧ �ѹ ��͹ �� ���� ���� ������ʴ� ================================
	// Creating dialog
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,mDay);
		case DATE_DIALOG_DATA_ID:
			return new DatePickerDialog(this, mDateDataSetListener, mYear, mMonth,mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,true);
		}
		return null;
	}
	//===================================��÷ӧҹ���ǹ�ͧ��駤�� �ѹ ��͹ �� =============================================
	//�Ѿഷ����Ѻ-��¨��� ����繵����¡�� �ѹ ����к�
	public void updateDataByDate() {
		listview_data.clear();							
		curDate = Integer.toString(mDay)+ "/"+ Integer.toString(mMonth+1)+"/"+ Integer.toString(mYear);
		listview_data = getAllActivity(dbHelp.getActivityListData(new Utility().getDateFormat(mDay, mMonth, mYear)));		
		txt_totalIncome.setText(dbHelp.getIncomeAmountByDate(curDate));
		txt_totalExpense.setText(dbHelp.getExpenseAmountByDate(curDate));
		listLayout.notifyDataSetChanged();
	}
	//�Ѿഷ �ѹ ��͹ �� ���������ٻẺ㹡�� �ʴ��͡�ҧ˹�Ҩ� ����繻Ѩ�غѹ
	public void updateDate() {
		this.txtDate.setText(new StringBuilder().append("�ѹ��� : ").append(mDay).append("/").append(mMonth + 1).append("/").append(mYear));
	}
	//===================================��÷ӧҹ���ǹ�ͧ��駤�� ���� =============================================
	// �Ѿഷ�������������ٻẺ㹡�� �ʴ��͡�ҧ˹�Ҩ� ����繻Ѩ�غѹ
	public void updateTime() {
		String hour = Integer.toString(mHour);
		String minute = Integer.toString(mMinute);
		if (mHour < 10)
			hour = "0" + Integer.toString(mHour);
		if (mMinute < 10)
			minute = "0" + Integer.toString(mMinute);
		this.txtTime.setText(new StringBuilder().append("���� : ").append(hour).append(".").append(minute));
	}
	//======================================��ǹ㹡�÷ӧҹ�ͧ ��ö����ٻ�˹�� �Ԩ����===================================== 
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case CAMERA_RESULT:
			if (resultCode == RESULT_OK) {
				try{
					Log.v("RESULTS", "HERE");
					bmp = BitmapFactory.decodeFile(getImagePath());
					bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/2, bmp.getHeight()/2, true);
					imageCam.setImageBitmap(bmp);
					Log.v("RESULTS", "Image Width: " + bmp.getWidth());
					Log.v("RESULTS", "Image Height: " + bmp.getHeight());
				}catch (Exception e) {
					Toast.makeText(getApplicationContext(),"�Դ��ͼԴ��Ҵ㹡���������� ��س��ͧ�����ա����", Toast.LENGTH_LONG).show();
					/*if(canEdit)
						showBalanceDialog(canEdit,balanceObject);
					else
						showBalanceDialog(canEdit,new BalanceObject());
					imageCam.setImageBitmap(bmp);*/
				}
			}
			break;
		}
	}
	public String getImagePath()
	{
		return  Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp_image.jpg";
	}
	//================================== ��ô֧�������Ҩҡ Database ================================================
	// method �����������㹡�ô֧ �ѭ�շ������շ��������ʴ� �˹�� ���� �Ԩ���� ���� ��䢡Ԩ����
	public String[] getBankInAccount(){
		List bankAcronymList = dbHelp.getBankAcronymInAccount();
		HashMap<String, Object> hmbank ;
		String bankName[] = new String[bankAcronymList.size()];
		for (int i = 0; i < bankAcronymList.size(); i++) {
			hmbank = (HashMap<String, Object>) bankAcronymList.get(i);
			bankName[i] =(String) hmbank.get("pathUsing");
		}
		return bankName;
	}
	// method �����������㹡�ô֧ accountId �ͧ�ѭ�շ�������͡ �˹�� ���� �Ԩ���� ���� ��䢡Ԩ����
	public String getAccountIdWithIndex(int index){
		List bankAcronymList = dbHelp.getBankAcronymInAccount();
		HashMap<String, Object> hmbank ;
		String accountId[] = new String[bankAcronymList.size()];
		for (int i = 0; i < bankAcronymList.size(); i++) {
			hmbank = (HashMap<String, Object>) bankAcronymList.get(i);
			accountId[i] = (String) hmbank.get(ACCOUNTIDKEY);
		}
		return accountId[index];
	}
	// ================================= Class ���Ӣ����ŷ������ hm ����� Listview ��������ʴ���˹�Ҩ���ѡ� Tab Activity=================================================
	
	private class ListViewLayout extends BaseAdapter {

		private ArrayList<HashMap<String, Object>> listview_data;
		private LayoutInflater mInflater;
		private int[] colors = new int[] { 0x30ffffff, 0x30808080 };

		public ListViewLayout(ArrayList<HashMap<String, Object>> listview_data,Context context) {
			this.listview_data = listview_data;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listview_data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listview_data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				final int row = position;
				ViewHolder holder;
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.listview_data_list_layout, null);
					holder = new ViewHolder();
					holder.banknameIMG = (ImageView) convertView.findViewById(R.id.listview_data_list_layout_img);
					holder.description = (TextView) convertView.findViewById(R.id.listview_data_list_layout_description);
					holder.date = (TextView) convertView.findViewById(R.id.listview_data_list_layout_date);
					holder.time = (TextView) convertView.findViewById(R.id.listview_data_list_layout_time);
					holder.edit = (Button) convertView.findViewById(R.id.listview_data_list_layout_ButtonEdit);
					holder.delete = (Button) convertView.findViewById(R.id.listview_data_list_layout_ButtonDelete);
					holder.amount = (TextView) convertView.findViewById(R.id.listview_data_list_layout_amount);
					holder.typeUsingIMG = (ImageView) convertView.findViewById(R.id.listview_data_list_layout_imgTypeUsinng);
					convertView.setTag(holder);

				} else {

					holder = (ViewHolder) convertView.getTag();
				}
				String typeUsing = (String) listview_data.get(position).get(TYPEUSINGKEY);
				//set ��ҷ������� hm ������ layout �ͧ listView
				holder.banknameIMG.setImageResource((Integer) listview_data.get(position).get(IMGKEY));	
				if(typeUsing.equals(FixTypeUsing.fixIncome))
					holder.typeUsingIMG.setImageResource(R.drawable.income);
				else if(typeUsing.equals(FixTypeUsing.fixExpense))
					holder.typeUsingIMG.setImageResource(R.drawable.expense);
				else if(typeUsing.equals(FixTypeUsing.fixWithdraw) || typeUsing.equals(FixTypeUsing.fixTransfer))
					holder.typeUsingIMG.setImageResource(R.drawable.transfer_withdraw);				
				holder.description.setText((String) listview_data.get(position).get(DESCRIPTIONKEY));
				holder.time.setText((String) listview_data.get(position).get(TIMEKEY)+" �.");
				holder.date.setText((String) listview_data.get(position).get(DATEKEY));
				holder.amount.setText((String) listview_data.get(position).get(AMOUNTKEY));
				//���������Ҥ��ط�� ��Ҩ��� ���� �Ѻ��� (ᴧ,����)
				if (typeUsing.equals(FixTypeUsing.fixIncome) ||  typeUsing.equals(FixTypeUsing.fixWithdraw)) {
					holder.amount.setTextColor(Color.rgb(74, 178, 0));					
				} else {
					holder.amount.setTextColor(Color.rgb(224, 88, 24));
				}
				//���� �ҡ�ҷ ������㹡��ź�����šԨ����
				holder.edit.setFocusableInTouchMode(false);
				holder.edit.setFocusable(false);
				holder.edit.setTag(position);
				holder.edit.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						try {
							balanceObject = new BalanceObject();
							balanceObject.setActivityId((String) listview_data.get(row).get(ACTIVITYIDKEY));
							balanceObject.setAccountId((String) listview_data.get(row).get(ACCOUNTIDKEY));
							balanceObject.setNetPrice((String) listview_data.get(row).get(AMOUNTKEY));
							balanceObject.setDate((String) listview_data.get(row).get(DATEKEY));
							balanceObject.setDescription((String) listview_data.get(row).get(DESCRIPTIONKEY));
							balanceObject.setTypeUsing((String) listview_data.get(row).get(TYPEUSINGKEY));
							balanceObject.setTime((String) listview_data.get(row).get(TIMEKEY));
							showBalanceDialog(true,balanceObject);
						} catch (Exception e) {
							AlertDialog.Builder b = new AlertDialog.Builder(BalanceActivity.this);
							b.setMessage(e.toString());
							b.show();
						}					
					}
				});
				//���� �ҡ�ҷ ������㹡��ź�����šԨ����
				holder.delete.setFocusableInTouchMode(false);
				holder.delete.setFocusable(false);
				holder.delete.setTag(position);
				holder.delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(BalanceActivity.this);
						builder.setMessage(
								"�س��ͧ���ź��¡�ù���������?\n- "
										+ listview_data.get((Integer) v.getTag()).get(DESCRIPTIONKEY)
										+ "\n- "
										+ listview_data.get((Integer) v.getTag()).get(TIMEKEY)
										+ "\n- "
										+ listview_data.get((Integer) v.getTag()).get(DATEKEY)).setCancelable(false)
								.setPositiveButton("��ŧ",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										hm = new HashMap<String, Object>();
										hm = listview_data.get(row);
										dbHelp.deleteActivity((String) hm.get(ACTIVITYIDKEY),(String) hm.get(ACCOUNTIDKEY),(String) hm.get(TYPEUSINGKEY),(String) hm.get(AMOUNTKEY));
										listview_data.remove(row);
										listLayout.notifyDataSetChanged();
										dialog.dismiss();
									}
								})
								.setNegativeButton("¡��ԡ",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();
					}
				});	
				//��Ѻ�մ� ��� ������
				int colorPos = position % colors.length;
				convertView.setBackgroundColor(colors[colorPos]);

			} catch (Exception e) {
				AlertDialog.Builder b = new AlertDialog.Builder(BalanceActivity.this);
				b.setMessage(e.toString());
				b.show();
			}
			return convertView;
		}

		class ViewHolder {
			ImageView banknameIMG;
			ImageView typeUsingIMG;
			TextView description;
			TextView date;
			TextView time;
			Button edit;
			Button delete;
			TextView amount;
		}

	}
}
