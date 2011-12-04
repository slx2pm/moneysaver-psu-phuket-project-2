package edusoft.android.database;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import edusoft.android.balance.BalanceActivity;
import edusoft.android.main.R;
import edusoft.android.reuse.AccountObject;
import edusoft.android.reuse.AccountTypeObject;
import edusoft.android.reuse.BalanceObject;
import edusoft.android.reuse.BankObject;
import edusoft.android.reuse.FixTypeUsing;
import edusoft.android.reuse.Utility;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

	ContentValues cv;
	BankObject bankObj;
	AccountTypeObject accTypeObj;
	AccountObject accObj;
	BalanceObject balObj;
	
	String bankName = "";
	List listAccount;
	List listAccountType;
	List listActivity;
	Utility uti = new Utility();
	
	public static final String dbName = "money_saver.db";
	
	public static final String bankTable = "bank";
	public static final String colbankId = "bank_id";
	public static final String colbankName = "bank_name";
	public static final String colbankAcronym = "bank_acronym";
	
	public static final String accountTypeTable = "fix_account_type";
	public static final String colAccountTypeId = "fix_account_type_id";
	public static final String colAccountType = "fix_account_type";
	
	public static final String accountTypeUsingTable = "fix_account_type_using";
	public static final String colAccountTypeUsingId = "fix_account_type_using_id";
	public static final String colAccountTypeUsing = "fix_account_type_using_description";
	
	public static final String accountTable = "account";
	public static final String colAccountId = "account_id";
	public static final String colAccountNumber = "account_number";
	public static final String colAccountName = "account_name";	
	public static final String colAccountCurrentBalance = "account_current_balance";
	public static final String colAccountLimitUsage = "account_limit_usage";
	
	public static final String activityTable = "activity";
	public static final String colActivityId = "activity_id";
	//public static final String colAccountTypeUsingId = "fix_account_type_using_id";
	public static final String colActivityDescription = "description";
	//public static final String colActivityPathUsing = "path_using";
	public static final String colActivityDate = "date";
	public static final String colActivityTime = "time";
	public static final String colActivityNetPrice = "net_price";

	public DatabaseHelper(Context context) {
		super(context, dbName, null, 33);

		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		
		try{
			
			db.execSQL("CREATE TABLE " + bankTable + " (" 
					+ colbankId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ colbankName + " TEX NOT NULL,"
					+ colbankAcronym + " TEX NOT NULL)");
			
			db.execSQL("CREATE TABLE " + accountTypeTable + " (" 
					+ colAccountTypeId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ colAccountType + " TEX NOT NULL)");
			
			db.execSQL("CREATE TABLE " + accountTypeUsingTable + " (" 
					+ colAccountTypeUsingId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ colAccountTypeUsing + " TEX NOT NULL)");
			
			db.execSQL("CREATE TABLE " + accountTable + " (" 
					+ colAccountId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ colbankId + " INTEGER,"
					+ colAccountTypeId + " INTEGER,"
					+ colAccountNumber + " TEX NOT NULL,"
					+ colAccountName + " TEX NOT NULL,"					
					+ colAccountLimitUsage + " NUMBER,"
					+ colAccountCurrentBalance + " NUMBER)");
			
			db.execSQL("CREATE TABLE " + activityTable + " (" 
					+ colActivityId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ colAccountId + " INTEGER , " 
					+ colAccountTypeUsingId + " TEXT NOT NULL,"
					+ colActivityDescription + " TEXT NOT NULL," 
					+ colActivityDate + " TEXT NOT NULL," 
					+ colActivityTime + " TEXT NOT NULL," 
					+ colActivityNetPrice+ " NUMERIC)");
			insertInitialData(db);
		}catch (Exception ex) {
			return;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + bankTable);
		db.execSQL("DROP TABLE IF EXISTS " + accountTable);
		db.execSQL("DROP TABLE IF EXISTS " + activityTable);
		onCreate(db);
	}
	
	public void insertInitialData(SQLiteDatabase db){
		
		//�����Ÿ�Ҥ��
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(0,'�Թʴ','cash')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(1,'��Ҥ�á�ا෾ �ӡѴ(��Ҫ�)','bbl')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(2,'��Ҥ�á�ԡ��� �ӡѴ(��Ҫ�)','kbank')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(3,'��Ҥ�á�ا�� �ӡѴ(��Ҫ�)','ktb')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(4,'��Ҥ�á�ا�����ظ�� �ӡѴ(��Ҫ�)','bay')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(5,'��Ҥ�����õԹҤԹ','kk')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(6,'��Ҥ�ëԵ��ầ�� �ӡѴ','citi')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(7,'��Ҥ�ë��������� ��','cimb')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(8,'��Ҥ�÷����� �ӡѴ(��Ҫ�)','tmb')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(9,'��Ҥ�÷���� �ӡѴ (��Ҫ�)','tisco')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(10,'��Ҥ���¾ҳԪ�� �ӡѴ(��Ҫ�)','scb')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(11,'��Ҥ�ø��ҵ�','tbank')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(12,'��Ҥ�ù����ǧ�� �ӡѴ(��Ҫ�)' ,'scib')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(13,'��Ҥ�������ҡžҳԪ�� ','megabank')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(14,'��Ҥ�����ͺ�','uob')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(15,'��Ҥ���ᵹ���촪���������','scbt')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(16,'��Ҥ������Թ','gsb')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(17,'��Ҥ����������觻������','ibank')");
		db.execSQL("insert into bank (bank_id,bank_name,bank_acronym) values(18,'��Ҥ���ͫպի�','icbc')");
		//�����Ū�Դ�ѭ��
		db.execSQL("insert into fix_account_type(fix_account_type_id,fix_account_type) values(0,'�Թʴ')");
		db.execSQL("insert into fix_account_type(fix_account_type_id,fix_account_type) values(1,'�ѭ�ա��������ѹ')");
		db.execSQL("insert into fix_account_type(fix_account_type_id,fix_account_type) values(2,'�ѭ�������Ѿ��')");
		db.execSQL("insert into fix_account_type(fix_account_type_id,fix_account_type) values(3,'�ѭ���Թ�ҡ��Ш�')");
		//�����Ż�������������
		db.execSQL("insert into fix_account_type_using(fix_account_type_using_id,fix_account_type_using_description) values(0,'����Ѻ')");
		db.execSQL("insert into fix_account_type_using(fix_account_type_using_id,fix_account_type_using_description) values(1,'��¨���')");
		db.execSQL("insert into fix_account_type_using(fix_account_type_using_id,fix_account_type_using_description) values(2,'�͹�Թ')");
		db.execSQL("insert into fix_account_type_using(fix_account_type_using_id,fix_account_type_using_description) values(3,'�͹�Թ')");
		
		
		//�����źѭ�� ʶҹ� �Թʴ
		db.execSQL("insert into account(account_id,bank_id,account_number,account_name,fix_account_type_id,account_limit_usage,account_current_balance) values(0,0,'1','cash',0,0.00,0.00)");
	}
	//================================ Bank ==========================================
	
	
	public BankObject getBankObjectByBankId(String bankId){
		bankObj = new BankObject();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select * from " + bankTable + " where "+colbankId + " = "+bankId, null);
		if(cur.getCount() > 0){
			cur.moveToFirst();
			bankObj.setBankId(bankId);
			bankObj.setBankName(cur.getString(cur.getColumnIndex(colbankName)));
			bankObj.setBankAcronym(cur.getString(cur.getColumnIndex(colbankAcronym)));
		}
		return bankObj;
	}
	
	public String[] getBankNameArray(){
		int i = 0;	
		bankObj = new BankObject();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select "+colbankName+" from " + bankTable , null);
		String [] bankName = new String[cur.getCount()-1];
		if(cur.getCount() > 0){	
			cur.moveToNext();
			while(cur.moveToNext()){
				if(new Utility().isNotNull(cur.getString(cur.getColumnIndex(colbankName))))
					bankName[i] = cur.getString(cur.getColumnIndex(colbankName));
				i++;
			}			
		}
		return bankName;
	}
	
	public String getBankAcronymByAccountId(String accId){
		String bankAcronym = "";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT "+colbankAcronym+" FROM bank,account,activity WHERE activity.account_id ="+accId+" AND activity.account_id = account.account_id AND account.bank_id = bank.bank_id", null);
		if(cur.getCount() > 0){
			cur.moveToFirst();
			bankAcronym = cur.getString(cur.getColumnIndex(colbankAcronym));
		}
		return bankAcronym;
	}
	
	public String getBanknameForSpinner(String accId){
		String bankAcronym = "";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT "+colAccountName+","+colbankName+","+colAccountId+" FROM bank,account WHERE account.bank_id = bank.bank_id AND account.account_id ="+accId, null);
		if(cur.getCount() > 0){
			cur.moveToFirst();
			bankAcronym = cur.getString(cur.getColumnIndex(colbankName)) +"["+cur.getString(cur.getColumnIndex(colAccountName))+"]";
		}
		return bankAcronym;
	}

	public List getBankAcronymInAccount(){
		List bankAcronym = new ArrayList<HashMap<String, Object>>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT "+colAccountName+","+colbankName+","+colAccountId+" FROM bank,account WHERE account.bank_id = bank.bank_id", null);
		HashMap<String, Object> hm ;
		if(cur.getCount() > 0){
			while(cur.moveToNext()){
				hm = new HashMap<String, Object>();
				hm.put("accountId", cur.getString(cur.getColumnIndex(colAccountId)));
				hm.put("pathUsing", cur.getString(cur.getColumnIndex(colbankName)) +"["+cur.getString(cur.getColumnIndex(colAccountName))+"]");
				bankAcronym.add(hm);
			}
		}
		return bankAcronym;
	}
	//================================ CATEGORY ===========================================
	
	
	public List getAccountTypeList(){
		listAccountType = new ArrayList();
		accTypeObj = new AccountTypeObject();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select * from " + accountTypeTable , null);
		if(cur.getColumnCount() > 0){		
			while(cur.moveToNext()){
				accTypeObj.setAccountTypeId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountTypeId))));
				accTypeObj.setAccountType(cur.getString(cur.getColumnIndex(colAccountType)));
				listAccountType.add(accTypeObj);
			}			
		}
		return listAccountType;
	}	
	
	public AccountTypeObject getAccTypeByAccountTypeId(String accTypeId){
		accTypeObj = new AccountTypeObject();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select "+colAccountType+" from " + accountTypeTable + " where "+colAccountTypeId + " = "+accTypeId, null);
		if(cur.getCount() > 0){
			cur.moveToFirst();
			accTypeObj.setAccountTypeId(accTypeId);
			accTypeObj.setAccountType(cur.getString(cur.getColumnIndex(colAccountType)));
		}
		return accTypeObj;
	}
	
	public String[] getAccTypeArray(){
		int i = 0;	
		bankObj = new BankObject();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select "+colAccountType+" from " + accountTypeTable , null);
		String [] accType = new String[cur.getCount()-1];
		if(cur.getCount() > 0){		
			cur.moveToNext();
			while(cur.moveToNext()){
				if(new Utility().isNotNull(cur.getString(cur.getColumnIndex(colAccountType))))
					accType[i] = cur.getString(cur.getColumnIndex(colAccountType));
				i++;
			}			
		}
		return accType;
	}
	//================================ ACCOUNT ============================================
	
	//�����ѭ�� ������¡�� (������ +)
	public String addAccount(AccountObject accObj){
		String accId = "";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("insert into account(bank_id,account_name,account_number,fix_account_type_id,account_limit_usage,account_current_balance) values(" 
				+ accObj.getBankId()+",'"+accObj.getAccountName()+"','"+accObj.getAccountNumber()+"',"+accObj.getAccountTypeId()+","+accObj.getLimitUsage()+","+accObj.getCurrentBalance()+")");
		Cursor cur = db.rawQuery("Select MAX("+colAccountId+") As "+colAccountId+" from " + accountTable , null);
		if(cur.getCount() == 1){
			cur.moveToFirst();
			accId = Integer.toString(cur.getInt(cur.getColumnIndex(colAccountId)));
		}
		return accId;
	}
	//ź�ѭ�� �������¡�� (������ x)
	public void deleteAccount(String accountId){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM account WHERE "+colAccountId+"="+accountId);
	}
	//���ǧ�Թ���ӡѴ੾�Тͧ �Թʴ
	public void editLimitedUsageForCash(int accId, double limiteUsage) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colAccountLimitUsage, limiteUsage);
		db.update(accountTable, cv, colAccountId + "=" + accId, null);
	}
	//���ǧ�Թ���ӡѴ�ͧ�ѭ�ո�Ҥ��
	public void editLimitedUsageForAccount(AccountObject accObj) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colbankId, Integer.parseInt(accObj.getBankId()));
		cv.put(colAccountTypeId, Integer.parseInt(accObj.getAccountTypeId()));
		cv.put(colAccountNumber, accObj.getAccountNumber());
		cv.put(colAccountName, accObj.getAccountName());
		cv.put(colAccountLimitUsage, Double.parseDouble(accObj.getLimitUsage()));
		cv.put(colAccountCurrentBalance, Double.parseDouble(accObj.getCurrentBalance()));		
		db.update(accountTable, cv, colAccountId + "=" + accObj.getAccountId(), null);
	}
	//�֧������ account ���������ʴ��˹���á�ͧ tab account
	public List getAccountListData(){
		listAccount = new ArrayList();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select * from " + accountTable , null);
		if(cur.getCount() > 0){
			while(cur.moveToNext()){
				accObj = new AccountObject(); 
				accObj.setAccountId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountId))));
				accObj.setAccountNumber(cur.getString(cur.getColumnIndex(colAccountNumber)));
				accObj.setAccountName(cur.getString(cur.getColumnIndex(colAccountName)));
				accObj.setBankId(Integer.toString(cur.getInt(cur.getColumnIndex(colbankId))));
				accObj.setAccountTypeId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountTypeId))));
				accObj.setLimitUsage(Double.toString(cur.getDouble(cur.getColumnIndex(colAccountLimitUsage))));
				accObj.setCurrentBalance((Double.toString(cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)))));
				listAccount.add(accObj);
			}
		}
		cur.close();
		return listAccount;
	}
	public String[] getAccountAllCategory(){
		int i = 0;	
		bankObj = new BankObject();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select "+colAccountType+" from " + accountTable , null);
		String [] category = new String[cur.getColumnCount()];
		if(cur.getColumnCount() > 0){		
			cur.moveToFirst();
			category[0] = cur.getString(cur.getColumnIndex(colAccountType));
			i++;
			while(cur.moveToNext()){
				category[i] = cur.getString(cur.getColumnIndex(colAccountType));
				i++;
			}			
		}
		return category;
	}	
	//�ӹǹ balance 㹺ѭ�����������Ѻ ���� ��¨���(����֧ �͹�Թ,�͹�Թ) 
	public boolean calculateAccBalanceForAdd(String accId,String typeUsing,String amount)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		boolean returnValue = false;
		double limit_usage = 0;
		double curBalance = 0;
		Cursor cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"="+accId, null);
		if(cur.getCount() == 1){
			cur.moveToFirst();
			curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance));
		}
		
		curBalance += Double.parseDouble(amount);
		
		ContentValues cv = new ContentValues();
		cv.put(colAccountCurrentBalance, curBalance);
		int rowEffected = db.update(accountTable, cv, colAccountId + "=" + accId, null);
		if(rowEffected > 0)
			returnValue = true;
		//��Ǩ�ͺ����繡�ö͹�Թ�����Ҷ͹��������仺ǡ� Cash
		
		if(typeUsing.equals("2")){
			cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"=0", null);
			if(cur.getCount() == 1){
				cur.moveToFirst();
				limit_usage = cur.getDouble(cur.getColumnIndex(colAccountLimitUsage))+ (Double.parseDouble(amount)*-1);
				curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)) + (Double.parseDouble(amount)*-1);
				cv = new ContentValues();
				cv.put(colAccountLimitUsage, limit_usage);
				cv.put(colAccountCurrentBalance, curBalance);
			}
			rowEffected = db.update(accountTable, cv, colAccountId + "=0", null);
		}
		return returnValue;
		
	}	
	//�ӹǹ balance 㹺ѭ�����������Ѻ ���� ��¨���(����֧ �͹�Թ,�͹�Թ)
	public boolean calculateAccBalanceForSameTypeEdit(String accId,String activityId,String typeUsing,String amount,String amountFromEdit)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv ;
		boolean returnValue = false;
		double limit_usage = 0;
		double curBalance = 0;
		
		Cursor cur = db.rawQuery("SELECT account_limit_usage,account_current_balance,fix_account_type_id FROM " + accountTable +" WHERE "+colAccountId+"="+accId, null);
		if(cur.getCount() == 1){
			cur.moveToFirst();
			curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance))+Double.parseDouble(amount);
		}		
		
		cv = new ContentValues();
		cv.put(colAccountCurrentBalance, curBalance);
		int rowEffected = db.update(accountTable, cv, colAccountId + "=" + accId, null);
		
		int oldTypeUsing = 0;
		cur = db.rawQuery("SELECT fix_account_type_using_id FROM " + activityTable +" WHERE "+colActivityId+"="+activityId, null);
		if(cur.getCount() == 1){
			cur.moveToFirst();
			oldTypeUsing = cur.getInt(cur.getColumnIndex(colAccountTypeUsingId));
		}

		//��Ǩ�ͺ����繡�ö͹�Թ�����Ҷ͹��������仺ǡ� Cash ��㹡���Ѿഷ������ͧ͢ Cash 
		if(typeUsing.equals("2") && oldTypeUsing != 2){ //�͹ �� �͹
			cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"=0", null);
			if(cur.getCount() == 1){
				cur.moveToFirst();
				limit_usage = cur.getDouble(cur.getColumnIndex(colAccountLimitUsage))+ (Double.parseDouble(amountFromEdit));
				curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)) + (Double.parseDouble(amountFromEdit));	
				cv = new ContentValues();
				cv.put(colAccountLimitUsage, limit_usage);
				cv.put(colAccountCurrentBalance, curBalance);
			}
			rowEffected = db.update(accountTable, cv, colAccountId + "=0", null);			
		}else if(oldTypeUsing == 2 && !typeUsing.equals("2")){ //�óն͹ �� �͹  �е�ͧ��Ҥ������������¹�ź Cash ����
			cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"=0", null);
			if(cur.getCount() == 1){
				cur.moveToFirst();
				limit_usage = cur.getDouble(cur.getColumnIndex(colAccountLimitUsage))+ (Double.parseDouble(amountFromEdit)*-1);
				curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)) + (Double.parseDouble(amountFromEdit)*-1);
				if(limit_usage < 0 && curBalance < 0 ) 
				{
					limit_usage = 0;
					curBalance = 0;
				}
				cv = new ContentValues();
				cv.put(colAccountLimitUsage, limit_usage);
				cv.put(colAccountCurrentBalance, curBalance);
			}
			rowEffected = db.update(accountTable, cv, colAccountId + "=0", null);
		}else if(typeUsing.equals("2") && oldTypeUsing == 2){ //�ó� �͹ �� �͹ ������¹����Ţ �����Ҽŵ�ҧ �Һǡ�Ѻ������ �� ��� 500 ����¹ �� 600 ������ 100 �Һǡ����
			cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"=0", null);
			if(cur.getCount() == 1){
				cur.moveToFirst();
				limit_usage = cur.getDouble(cur.getColumnIndex(colAccountLimitUsage))+ (Double.parseDouble(amount)*-1);
				curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)) + (Double.parseDouble(amount)*-1);	
				cv = new ContentValues();
				cv.put(colAccountLimitUsage, limit_usage);
				cv.put(colAccountCurrentBalance, curBalance);
			}
			rowEffected = db.update(accountTable, cv, colAccountId + "=0", null);			
		}
		
		if(rowEffected > 0)
			returnValue = true;
		return returnValue;
		
	}
	//�ӹǹ balance 㹺ѭ�����������Ѻ ���� ��¨���(����֧ �͹�Թ,�͹�Թ) 
	public boolean calculateAccBalanceForDifferentTypeEdit(String accId,String activityId,String typeUsing,String amount,String amountFromEdit,String amountBeforEdit)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		boolean returnValue = false;
		double limit_usage = 0;
		double curBalance = 0;

		Cursor cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"="+accId, null);
		if(cur.getCount() == 1){
			cur.moveToFirst();
			curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance))+Double.parseDouble(amount);
		}
					
		cv = new ContentValues();
		cv.put(colAccountCurrentBalance, curBalance);
		int rowEffected = db.update(accountTable, cv, colAccountId + "=" + accId, null);
		
		int oldTypeUsing = 0;
		cur = db.rawQuery("SELECT fix_account_type_using_id FROM " + activityTable +" WHERE "+colActivityId+"="+activityId, null);
		if(cur.getCount() == 1){
			cur.moveToFirst();
			oldTypeUsing = cur.getInt(cur.getColumnIndex(colAccountTypeUsingId));
		}

		//��Ǩ�ͺ����繡�ö͹�Թ�����Ҷ͹��������仺ǡ� Cash ��㹡���Ѿഷ������ͧ͢ Cash 
		if(typeUsing.equals("2") && oldTypeUsing != 2){ //����Ѻ �� ���� ���������ҹ� �Ѻ --> �͹
			cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"=0", null);
			if(cur.getCount() == 1)
			{
				cur.moveToFirst();
				limit_usage = cur.getDouble(cur.getColumnIndex(colAccountLimitUsage))+ (Double.parseDouble(amountFromEdit));
				curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)) + (Double.parseDouble(amountFromEdit));	
				cv = new ContentValues();
				cv.put(colAccountLimitUsage, limit_usage);
				cv.put(colAccountCurrentBalance, curBalance);
			}
			
			rowEffected = db.update(accountTable, cv, colAccountId + "=0", null);			
		}
		else if(oldTypeUsing == 2)//��¨��� ���Ѻ �������ʶҹ� �͹ --> �Ѻ
		{
			cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"=0", null);
			if(cur.getCount() == 1)
			{
				cur.moveToFirst();
				limit_usage = cur.getDouble(cur.getColumnIndex(colAccountLimitUsage))- (Double.parseDouble(amountBeforEdit));
				curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)) - (Double.parseDouble(amountBeforEdit));	
				cv = new ContentValues();
				cv.put(colAccountLimitUsage, limit_usage);
				cv.put(colAccountCurrentBalance, curBalance);
			}
			
			rowEffected = db.update(accountTable, cv, colAccountId + "=0", null);	
		}
		//*/
		
		
		if(rowEffected > 0)
			returnValue = true;
		
		return returnValue;
		
	}
	// �ӹǹ�ѭ�������͹����������ѭ������ 
	public boolean calculateAccBalanceForDifferentPathUsingEdit(BalanceObject oldBalObj,BalanceObject newBalObj){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		boolean returnValue = false;
		int rowEffected=0;
		double curBalance = 0;
		double limitUsage = 0;
		double amount=Double.parseDouble(newBalObj.getNetPrice());;
		Cursor cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"="+oldBalObj.getAccountId(), null);
		if(cur.getCount() == 1){
			cur.moveToFirst();
			curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance));
			limitUsage = cur.getDouble(cur.getColumnIndex(colAccountLimitUsage));
		}
		
		if(oldBalObj.getTypeUsing().equals(FixTypeUsing.fixIncome))
		{
			curBalance -= Double.parseDouble(oldBalObj.getNetPrice());
		}
		else
		{
			curBalance += Double.parseDouble(oldBalObj.getNetPrice());
			limitUsage += Double.parseDouble(oldBalObj.getNetPrice());
		}
		cv = new ContentValues();
		cv.put(colAccountLimitUsage, limitUsage);
		cv.put(colAccountCurrentBalance, curBalance);
		//�͹ �� �͹ ������¹�ѭ��
		if(oldBalObj.getTypeUsing().equals(FixTypeUsing.fixWithdraw) && newBalObj.getTypeUsing().equals(FixTypeUsing.fixWithdraw))
			amount = Double.parseDouble(oldBalObj.getNetPrice())- Double.parseDouble(newBalObj.getNetPrice());
		else if(!newBalObj.getTypeUsing().equals("0")) 				
			amount = amount*-1;
		rowEffected = db.update(accountTable, cv, colAccountId + "="+oldBalObj.getAccountId(), null);	
		calculateAccBalanceForAdd(newBalObj.getAccountId(),newBalObj.getTypeUsing(),Double.toString(amount));
		if(rowEffected > 0)
			returnValue = true;
		return returnValue;
	}
	//�ӹǹ balance 㹺ѭ�����������Ѻ ���� ��¨���(����֧ �͹�Թ,�͹�Թ) �� isPaid ��� ����Ѻ = false, ��¨��� = true ���������ǹ�ͧ���ź�Ԩ����
	public boolean calculateAccBalanceForDelete(String accountId,String typeUsing,String amount)
	{
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues cv;
				boolean returnValue = false;
				double limit_usage = 0;
				double curBalance = 0;
				int rowEffected = 0;
				Cursor cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"="+accountId, null);
				if(cur.getCount() == 1){
					cur.moveToFirst();
					curBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance));
				}
				
				if(typeUsing.equals("0"))//�ó��Ѻ
				{
					curBalance -= Double.parseDouble(amount);				
				}
				else if(typeUsing.equals("2"))//�óն͹
				{				
					cur = db.rawQuery("SELECT account_limit_usage,account_current_balance FROM " + accountTable +" WHERE "+colAccountId+"=0", null);
					cur.moveToFirst();
					double cashlimit_usage = cur.getDouble(cur.getColumnIndex(colAccountLimitUsage)) -  (Double.parseDouble(amount));
					double casgcurBalance = cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)) - (Double.parseDouble(amount));	
					cv = new ContentValues();
					cv.put(colAccountLimitUsage, cashlimit_usage);
					cv.put(colAccountCurrentBalance, casgcurBalance);
					rowEffected = db.update(accountTable, cv, colAccountId + "=0", null);
					//�ǡ �ӹǹ������� �Ѻ ǧ�Թ㹺ѭ�չ�� �
					curBalance += Double.parseDouble(amount);
				}
				else//�óը���
				{
					//�ǡ �ӹǹ������� �Ѻ ǧ�Թ㹺ѭ�չ�� �
					curBalance += Double.parseDouble(amount);
				}

				cv = new ContentValues();
				cv.put(colAccountCurrentBalance, curBalance);
				rowEffected = db.update(accountTable, cv, colAccountId + "=" + accountId, null);
				if(rowEffected > 0)
					returnValue = true;
				return returnValue;
				
	}	
	//================================ Activity ===========================================
	
	
	//�����Ԩ���� ������¡�� (������ +)
	public boolean addActivity(BalanceObject balObj){
		boolean returnValue=false;
		String activityId ="";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO activity(account_id,fix_account_type_using_id,description,date,time,net_price) VALUES(" 
				+ balObj.getAccountId()+","+balObj.getTypeUsing()+",'"+balObj.getDescription()+"','"+balObj.getDate()+"','"+balObj.getTime()+"',"+balObj.getNetPrice()+")");		
		double addNetPrice = Double.parseDouble(balObj.getNetPrice());
		//��Ǩ�ͺ���������Ѻ������¨���
		if(!balObj.getTypeUsing().equals("0")) 				addNetPrice = addNetPrice*-1;				
		return calculateAccBalanceForAdd(balObj.getAccountId(),balObj.getTypeUsing(),Double.toString(addNetPrice)) ;
	}	
	//ź�Ԩ���� �������¡�� (������ x)
	public void deleteActivity(String activityId,String accountId,String typeUsing,String amount){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM activity WHERE "+colActivityId+"="+activityId);
		calculateAccBalanceForDelete(accountId,typeUsing,amount);
	}
	//����������´�ͧ�Ԩ����
	public void editActivity(BalanceObject balObj) {
		BalanceObject balDetail = getActivityDatailWithId(balObj.getActivityId());
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colAccountId, Integer.parseInt(balObj.getAccountId()));
		cv.put(colAccountTypeUsingId, Integer.parseInt(balObj.getTypeUsing()));
		cv.put(colActivityDescription, balObj.getDescription());
		cv.put(colActivityDate, balObj.getDate());
		cv.put(colActivityTime, balObj.getTime());
		cv.put(colActivityNetPrice, Double.parseDouble(balObj.getNetPrice()));
		//��䢺ѭ�����ǡѹ
		if(balDetail.getAccountId().equals(balObj.getAccountId()))
		{
			 // ���� �� ���� (������¹����Ţ) ���� �Ѻ �� �Ѻ (������¹����Ţ)
			if((Integer.parseInt(balDetail.getTypeUsing()) == 0 && Integer.parseInt(balObj.getTypeUsing()) == 0) ||
			  (Integer.parseInt(balDetail.getTypeUsing()) > 0 && Integer.parseInt(balObj.getTypeUsing()) > 0)	)
			{
				double editNetPrice = (Double.parseDouble(balDetail.getNetPrice()) - Double.parseDouble(balObj.getNetPrice()));
				//�Ѻ �� �Ѻ  = ( ��� - ���� ) * -1
				if(Integer.parseInt(balObj.getTypeUsing()) ==0) 				editNetPrice = editNetPrice*-1;				
				calculateAccBalanceForSameTypeEdit(balObj.getAccountId(),balObj.getActivityId(),balObj.getTypeUsing(),Double.toString(editNetPrice),balObj.getNetPrice());
			}
			else // ���� �� �Ѻ ���� �Ѻ �� ���� 
			{
				double editNetPrice = Double.parseDouble(balDetail.getNetPrice()) + Double.parseDouble(balObj.getNetPrice());
				//�Ѻ �� ���� = (��� + �ͧ����)*-1
				if(Integer.parseInt(balDetail.getTypeUsing()) ==0 )				editNetPrice = editNetPrice*-1;
				calculateAccBalanceForDifferentTypeEdit(balObj.getAccountId(),balObj.getActivityId(),balObj.getTypeUsing(),Double.toString(editNetPrice),balObj.getNetPrice(),balDetail.getNetPrice());				
			}
		}
		else // ���Ẻ����¹�ѭ��
		{
			calculateAccBalanceForDifferentPathUsingEdit(balDetail,balObj);
			
		}
		int update = db.update(activityTable, cv, colActivityId + "=" + balObj.getActivityId(), null);
	}
	
	//�֧��¨��¢ͧ��͹�Ѩ�غѹ������ ᶺ�� ˹�� account
	public String getAmountExpenseInCurrentMonth(String month,String accountId){
		String expenseAmount = ""; 
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT SUM(net_price) AS expense FROM "+activityTable+" WHERE "+colAccountTypeUsingId+" <> '0' AND "+colActivityDate+" like '%/"+month+"/%' AND "+colAccountId+"="+accountId, null);
		if(cur.getCount() == 1){
			cur.moveToFirst();
			expenseAmount = Double.toString(cur.getDouble(cur.getColumnIndex("expense")));				
		}
		cur.close();
		return expenseAmount;	
	}
	//�֧������ activity ���������ʴ��˹���á�ͧ tab balance
	public List getActivityListData(String curDate){
		listActivity = new ArrayList();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + activityTable + " WHERE "+colActivityDate +"='"+curDate+"' ORDER BY "+colActivityDate+","+colActivityTime, null);
		if(cur.getCount() > 0){
			while(cur.moveToNext()){
				balObj = new BalanceObject(); 
				balObj.setActivityId(Integer.toString(cur.getInt(cur.getColumnIndex(colActivityId))));
				balObj.setAccountId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountId))));//setAccountId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountId))));
				balObj.setTypeUsing(cur.getString(cur.getColumnIndex(colAccountTypeUsingId)));//setBankId(Integer.toString(cur.getInt(cur.getColumnIndex(colbankId))));
				balObj.setDescription(cur.getString(cur.getColumnIndex(colActivityDescription)));//setAccountTypeId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountTypeId))));
				balObj.setDate(cur.getString(cur.getColumnIndex(colActivityDate)));//setCurrentBalance((Double.toString(cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)))));
				balObj.setTime(cur.getString(cur.getColumnIndex(colActivityTime)));
				balObj.setNetPrice(Double.toString(cur.getDouble(cur.getColumnIndex(colActivityNetPrice))));
				listActivity.add(balObj);
			}
		}
		cur.close();
		return listActivity;
	}
	//�ӹǹ�������ҡ����������繡����������Ţ ���� Ŵ����Ţ ���ͨ�任�Ѻ㹺ѭ�� account ��Ҥ�è� + ���� - �� ������� 100 �ҷ �������� 200 ����Ҩ������� 100 �ҷ ����� 100 �ҷ����ź� balance �ͧ account ��� �  
	public BalanceObject getActivityDatailWithId(String activityId)
	{
		BalanceObject balObj = new BalanceObject();
		double net_price = 0.0;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + activityTable +" WHERE "+colActivityId+"="+activityId, null);
		if(cur.getCount() == 1){
			cur.moveToFirst();
			balObj = new BalanceObject(); 
			balObj.setActivityId(Integer.toString(cur.getInt(cur.getColumnIndex(colActivityId))));
			balObj.setAccountId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountId))));//setAccountId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountId))));
			balObj.setTypeUsing(cur.getString(cur.getColumnIndex(colAccountTypeUsingId)));//setBankId(Integer.toString(cur.getInt(cur.getColumnIndex(colbankId))));
			balObj.setDescription(cur.getString(cur.getColumnIndex(colActivityDescription)));//setAccountTypeId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountTypeId))));
			balObj.setDate(cur.getString(cur.getColumnIndex(colActivityDate)));//setCurrentBalance((Double.toString(cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)))));
			balObj.setTime(cur.getString(cur.getColumnIndex(colActivityTime)));
			balObj.setNetPrice(Double.toString(cur.getDouble(cur.getColumnIndex(colActivityNetPrice))));
		}
		return balObj;
	}
	//=============================== REUSE ===============================================

	
	
	/*public ContentValues putAccObjToContentValue(AccountObject accObj){
		cv = new ContentValues();
		cv.put(colAccountId, Integer.parseInt(accObj.getAccountId()));
		cv.put(colbankId, Integer.parseInt(accObj.getBankId()));
		cv.put(colAccountType, accObj.getAccountTypeId());
		cv.put(colAccountLimitUsage, Double.parseDouble(accObj.getLimitUsage()));
		cv.put(colAccountCurrentBalance, Double.parseDouble(accObj.getCurrentBalance()));
		return cv;
	}*/
	/*public ContentValues putBalObjToContentValue(BalanceObject balObj){
		cv = new ContentValues();
		cv.put(colAccountId, Integer.parseInt(balObj.getAccountId()));
		cv.put(colAccountTypeUsingId, balObj.getTypeUsing());
		cv.put(colActivityDescription, balObj.getDescription());
		cv.put(colActivityPathUsing, balObj.getPathUsing());
		cv.put(colActivityDate, balObj.getDate());
		cv.put(colActivityTime, balObj.getTime());
		cv.put(colActivityNetPrice, Double.parseDouble(balObj.getNetPrice()));
		return cv;
	}*/
	
	/*public String[] convertListAccTypeToAccTypeIdArray(List listAccountType){
		accTypeObj = new AccountTypeObject();
		String[] accTypeId = new String[listAccountType.size()];
		for (int i = 0; i < listAccountType.size(); i++) {
			accTypeObj =(AccountTypeObject) listAccountType.get(i);
			accTypeId[i] = accTypeObj.getAccountTypeId();
		}
		return accTypeId;
	}*/
	
	/*public String[] convertListAccTypeToAccTypeArray(List listAccountType){
		accTypeObj = new AccountTypeObject();
		String[] accType = new String[listAccountType.size()];
		for (int i = 0; i < listAccountType.size(); i++) {
			accTypeObj =(AccountTypeObject) listAccountType.get(i);
			accType[i] = accTypeObj.getAccountType();
		}
		return accType;
	}*/
	

	/*public AccountObject curToAccObj(Cursor cur){
		cur.moveToFirst();
		accObj.setAccountId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountId))));
		accObj.setBankId(Integer.toString(cur.getInt(cur.getColumnIndex(colbankId))));
		accObj.setAccountTypeId(Integer.toString(cur.getInt(cur.getColumnIndex(colAccountTypeId))));
		accObj.setLimitUsage(Double.toString(cur.getDouble(cur.getColumnIndex(colAccountLimitUsage))));
		accObj.setCurrentBalance((Double.toString(cur.getDouble(cur.getColumnIndex(colAccountCurrentBalance)))));
		return accObj;
	}*/
	
	

	
	
	
	/*void AddRestaurant(String[][] dataSet) throws IOException {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		for (int i = 1; i < dataSet.length; i++) {
			for (int j = 0; j < dataSet[i].length; j++) {
				switch (j) {
				case 0:
					cv.put(colRDescription, dataSet[i][j]);
					break;
				case 1:
					cv.put(colRType, dataSet[i][j]);
					break;
				case 3:
					cv.put(colRName, dataSet[i][j]);
					break;
				case 4:
					cv.put(colRAddress, dataSet[i][j]);
					break;
				case 5:
					cv.put(colRTel, dataSet[i][j]);
					break;
				case 6:
					cv.put(colRWebsite, dataSet[i][j]);
					break;
				case 7:
					cv.put(colRRatio, dataSet[i][j]);
					break;
				case 8:
					cv.put(colRDateregist, dataSet[i][j]);
					break;
				case 9:
					cv.put(colRLat, dataSet[i][j]);
					break;
				case 10:
					cv.put(colRLong, dataSet[i][j]);
					break;
				case 11:
					cv.put(colRPic, dataSet[i][j]);
					break;
				default:
					break;
				}
			}
			db.insert(restaurantTable, colRName, cv);
		}
		db.close();

	}

	void AddFood(String[][] dataSet) throws IOException {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		for (int i = 1; i < dataSet.length; i++) {
			for (int j = 0; j < dataSet[i].length; j++) {
				switch (j) {
				case 0:
					cv.put(colFName, dataSet[i][j]);
					break;
				case 1:
					cv.put(colFDescription, dataSet[i][j]);
					break;
				case 2:
					cv.put(colFRatio, dataSet[i][j]);
					break;
				case 3:
					cv.put(colFPic, dataSet[i][j]);
					break;
				case 4:
					cv.put(colFRName, dataSet[i][j]);
					break;
				default:
					break;
				}
			}
			db.insert(foodTable, colFName, cv);
		}
		db.close();

	}

	int getRestaurantCount() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select * from " + restaurantTable, null);
		int x = cur.getCount();
		cur.close();
		return x;
	}

	Cursor getImages(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur;
		String WHERE = "FRNAME='" + name + "'";
		String[] columns = new String[] { colFName, colFRatio, colFPic };
		cur = db.query(foodTable, columns, WHERE, null, null, null, null);
		return cur;
	}

	Cursor getAllNearBy(String type, double lat_here, double long_here,
			double distanceLat, double distanceLong) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur;
		String WHERE;
		if (type.equals("All")) {
			WHERE = "RLAT BETWEEN " + (lat_here - distanceLat) + " and "
					+ (lat_here + distanceLat) + " and " + "RLONG BETWEEN "
					+ (long_here - distanceLong) + " and "
					+ (long_here + distanceLong);
			String[] columns = new String[] { colRName, colRRatio,
					colRDescription, colRTel, colRLat, colRLong };
			cur = db.query(restaurantTable, columns, WHERE, null, null, null,
					null);
			// cur = db.q
		} else {
			WHERE = "RTYPE ='" + type + "' and RLAT BETWEEN "
					+ (lat_here - distanceLat) + " and "
					+ (lat_here + distanceLat) + " and " + "RLONG BETWEEN "
					+ (long_here - distanceLong) + " and "
					+ (long_here + distanceLong);
			String[] columns = new String[] { colRName, colRRatio,
					colRDescription, colRTel, colRLat, colRLong };
			cur = db.query(restaurantTable, columns, WHERE, null, null, null,
					null);
		}
		return cur;

	}

	Cursor getThisRestuarant(String type) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur;
		String WHERE;
		WHERE = "RNAME ='" + type + "'";
		String[] columns = new String[] { colRId, colRName, colRRatio,
				colRDescription, colRTel, colRLat, colRLong };
		cur = db.query(restaurantTable, columns, WHERE, null, null, null, null);
		return cur;

	}

	Cursor getThisFood(String frname, String fname) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur;
		String WHERE;
		WHERE = "FRNAME ='" + frname + "'";
		String[] columns = new String[] { colFName, colFRatio, colFDescription,
				colFPic, colFRName };
		cur = db.query(foodTable, columns, WHERE, null, null, null, null);
		return cur;

	}

	Cursor getRestuarantName() {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur;
		String[] columns = new String[] { colRName };
		cur = db.query(restaurantTable, columns, null, null, null, null, null);
		return cur;
	}

	public String[] getRNAME() {
		SQLiteDatabase db = this.getReadableDatabase();
		// Cursor cur = dbHelp.getRestuarantName();
		// String[] nameArray ;
		String[] columns = new String[] { colRName };
		Cursor cur = db.query(restaurantTable, columns, null, null, null, null,
				null);
		String[] nameArray = new String[cur.getCount()];
		// cur.moveToFirst();
		int index = 0;
		while (cur.moveToNext()) {
			// Get the field values
			nameArray[index] = cur.getString(cur.getColumnIndex(colRName));
			index++;
		}
		return nameArray;
	}

	public int UpdateRestRating(int RID, float ratio) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colRRatio, ratio);
		return db.update(restaurantTable, cv, colRId + "=" + RID, null);
	}

	public int UpdateFoodRating(String frname, String fname, float ratio) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colFRatio, ratio);
		return db.update(foodTable, cv, colFRName + "='" + frname
				+ "' and FNAME='" + fname + "'", null);
	}*/

}
