package com.atm.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class ATM_Interface {




	public static void main(String[] args) throws NumberFormatException, IOException, SQLException, ParseException {
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("++++++++++++++++++++++WELCOME TO XYZ BANK ATM ++++++++++++++++++++");

			System.out.print("\t\t Enter your Account number:");
			String Accountnumber=br.readLine();
			System.out.print("\t\t Enter your secret pin:");
			String secretpin=br.readLine();
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			Connection conn=MysqlConnection.getConnection();
			PreparedStatement ps=conn.prepareStatement("select * from customers where accountnumber=?");
			ps.setString(1,Accountnumber);
			ResultSet result=ps.executeQuery();
			while(result.next())
			{
				String usersecretpin1=result.getString("secretpin");
			     String Accountname=result.getString("cusname");
			   
		 
		if(secretpin.equals(usersecretpin1) )
			
		{
			System.out.println("Welcome"+"\t"+( Accountname));
			System.out.println();
			
			boolean login=true;
			do
			{	
							System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
							System.out.println("1  --->   Deposit");
							System.out.println("2  --->   Withdraw");
							System.out.println("3  --->   Fund Transfer");
							System.out.println("4  --->   Balance Check");
							System.out.println("5  --->   Change Secretpin");
							System.out.println("6  --->   Transaction History");
							System.out.println("7  --->   Exit / Logout");
							System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
							System.out.print("\t\t Enter your choice:"); 
							int operationNumber=Integer.parseInt(br.readLine());
							String status=null;
							
							switch(operationNumber)
							{
								case 1: System.out.println("Enter deposit amount:");
										double depositAmount=Double.parseDouble(br.readLine());
										
										if(depositAmount>0)
										{
											conn=MysqlConnection.getConnection();
											ps=conn.prepareStatement("select * from customers where accountnumber =?");
											ps.setString(1, Accountnumber);
											result=ps.executeQuery();
											
											double balance=0.0;
											long cusid=0;
											while(result.next())
											{
												balance=result.getDouble("balance");
												cusid=result.getLong("cusid");
											}
											
											balance=balance+depositAmount;
											
											ps=conn.prepareStatement("update customers set balance=? where accountnumber=?");
											ps.setDouble(1, balance);
											ps.setString(2, Accountnumber);
											
											if(ps.executeUpdate()>0)
											{
												ps=conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
												Timestamp timestamp = new Timestamp(System.currentTimeMillis());
												String transactionId="TN"+timestamp.getTime(); 
												ps.setString(1, transactionId);
												ps.setDouble(2, depositAmount);
												ps.setDate(3, new Date(System.currentTimeMillis()));
												ps.setString(4, "deposit");
												ps.setLong(5,cusid);
												ps.setLong(6,cusid);
												
												ps.executeUpdate();
						
												
												System.out.println("Balance Updated!!");
												System.out.println("New Balance: "+balance);
											}
											else
											{
												System.out.println("Something went wrong!!");
											}
											
										}
										
										System.out.println("Do you want to continue??(Y/N)");
										 status=br.readLine();
										
										if(status.equals("n") || status.equals("N"))
										{
											login=false;
										}
										
										break;
								case 2:	 System.out.println("Enter Withdrawal amount:");
								 double withdrawalAmount=Double.parseDouble(br.readLine());
								 if(withdrawalAmount>0)
								 {
								    conn=MysqlConnection.getConnection();
									ps=conn.prepareStatement("select * from customers where accountnumber=?");
									ps.setString(1, Accountnumber);
									result=ps.executeQuery();
									
									double balance=0.0;
									while(result.next())
									{
										balance=result.getDouble("balance");
										result.getLong("accountnumber");
									}
									
									
									if(balance>withdrawalAmount)
									{
										balance=balance-withdrawalAmount;
										ps=conn.prepareStatement("update customers set balance=? where accountnumber=?");
										ps.setDouble(1, balance);
										ps.setString(2,Accountnumber);
										
										if(ps.executeUpdate()>0)
										{
											ps=conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
											Timestamp timestamp = new Timestamp(System.currentTimeMillis());
											String transactionId="TN"+timestamp.getTime(); //TN3243432432423
											ps.setString(1, transactionId);
											ps.setDouble(2, withdrawalAmount);
											ps.setDate(3, new Date(System.currentTimeMillis()));
											ps.setString(4, "withdraw");
											ps.setString(5,Accountnumber);
											ps.setString(6,Accountnumber);
											
											ps.executeUpdate();
											
											System.out.println("Balance Updated!!");
											System.out.println("New Balance: "+balance);
										}
										else
										{
											System.out.println("Something went wrong!!");
										}
									}
									else
									{
										System.out.println("Insufficient Balance!!");
									}

								 }
								 System.out.println("Do you want to continue??(Y/N)");
								 status=br.readLine();
									
									if(status.equals("n") || status.equals("N"))
									{
										login=false;
									}
								 	
								 	break;
							
								case 3: System.out.println("Please enter the receiver account Id:");
						    		long rcveId=Long.parseLong(br.readLine());
						    		
						    		System.out.println("Enter the amount:");
						    		double amount=Double.parseDouble(br.readLine());
						    		
						    		conn=MysqlConnection.getConnection();
						    		
						    		long receiverId=0;
						    		
									ps=conn.prepareStatement("select * from customers where accountnumber=?");
									ps.setLong(1, rcveId);
									result=ps.executeQuery();
									
									while(result.next())
									{
										receiverId=result.getLong("accountnumber");
									}
									
									double Balance=0.0;
									long senderId=0;
									ps=conn.prepareStatement("select balance,cusId from customers where accountnumber=?");
									ps.setString(1, Accountnumber);
									result=ps.executeQuery();
									
									while(result.next())
									{
										Balance=result.getDouble("balance");
										senderId=result.getLong("cusId");
									}
									
									if(receiverId==0)
									{
										System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
										System.out.println("Wrong receiver id!!");
										System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				

									}
									else if(Balance==0 || Balance<amount)
									{
										System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
										System.out.println("Insufficient account balance!!");
										System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				

									}
									else
									{
									   Balance=Balance-amount;
										ps=conn.prepareStatement("update customers set balance=? where accountnumber=?");
										ps.setDouble(1, Balance);
										ps.setString(2, Accountnumber);
										
										if(ps.executeUpdate()>0)
										{
											ps=conn.prepareStatement("select Balance from customers where accountnumber=?");
											ps.setLong(1, rcveId);
											double rcvBalance=0.0;
											result=ps.executeQuery();
											while(result.next())
											{
												rcvBalance=result.getDouble("balance");
											}
											
											rcvBalance=rcvBalance + amount;
											
											ps=conn.prepareStatement("update customers set balance=? where accountnumber=?");
											ps.setDouble(1, rcvBalance);
											ps.setLong(2, receiverId);
											
											if(ps.executeUpdate()>0)
											{
												 ps=conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
					                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					                                String transactionId="TN"+timestamp.getTime(); //TN3243432432423
					                                ps.setString(1, transactionId);
					                                ps.setDouble(2, amount);
					                                ps.setDate(3, new Date(System.currentTimeMillis()));
					                                ps.setString(4, "fund transfer");
					                                ps.setLong(5,senderId);
					                                ps.setLong(6,rcveId);
					                                
					                                ps.executeUpdate();
												System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
												System.out.println("Transaction Completed!!");
												System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				

											}
											else
											{
												System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
												System.out.println("Transaction Failed!!");
												System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				

											}
											
										}
										
										
									}
									System.out.println("Do you want to continue??(Y/N)");
									 status=br.readLine();
									
									if(status.equals("n") || status.equals("N"))
									{
										login=false;
									}
									
									break;
									

						    		
			
						   
								case 4:	conn=MysqlConnection.getConnection();
										ps=conn.prepareStatement("select balance from customers where accountnumber =?");
										ps.setString(1,Accountnumber);
										result=ps.executeQuery();
										
										double balance=0.0;
										while(result.next())
										{
											balance=result.getDouble("balance");
										}
										System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
										System.out.println("Current Available Balance:"+balance);
										System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				

										 System.out.println("Do you want to continue??(Y/N)");
										 status=br.readLine();
											
											if(status.equals("n") || status.equals("N"))
											{
												login=false;
											}
										 	
										 	break;
								
								case 5: System.out.println("Please enter the existing secretpin: ");
										
								String existingsecretpin =br.readLine();
										
										System.out.println("Set new secretpin:");
								String newsecretpin1=br.readLine();
										
										System.out.println("Retype new secretpin:");
								String retypenewsecretpin=br.readLine();
										
										
										ps=conn.prepareStatement("select secretpin from customers where accountnumber=?");
										ps.setString(1, Accountnumber);
										
										result=ps.executeQuery();
									
										while(result.next())
										{
											String newsecretpin=result.getString("secretpin");
										
										
										if(existingsecretpin.equals(newsecretpin))
										{
											if(newsecretpin1.equals(retypenewsecretpin))
											{
												ps=conn.prepareStatement("update customers set secretpin=? where accountnumber=?");
												ps.setString(1, newsecretpin1);
												ps.setString(2, Accountnumber);
												
												if(ps.executeUpdate()>0)
												{
													System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
													System.out.println("Secretpin Changed!!");
													System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
													
												}
												
												else
												{
													System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
													System.out.println("Error in Secretpin change!!");
													System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
													
												}
											}
											else
											{
												System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
												System.out.println("Set new Secretpin and retype Secretpin must be same!!");
												System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
												
											}
										}
										else
										{
											System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
											System.out.println("Please enter correct existing Secretpin!!");
											System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
											
								
										}
										
										System.out.println("Do you want to continue??(Y/N)");
										status=br.readLine();
										System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				

										if(status.equals("n") || status.equals("N"))
										{
											login=false;
										}}
										break;
										
								case 6: ps=conn.prepareStatement("select * from customers where accountnumber=?");
								ps.setString(1, Accountnumber);
								
								result=ps.executeQuery();
								long cusId=0;
								while(result.next())
								{
									cusId=result.getLong("cusId");
								}
								if(cusId!=0)
								{
									ps=conn.prepareStatement("select * from transactions where senderAccountId=?");
									ps.setLong(1, cusId);
									
									result=ps.executeQuery();
									System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");	
									System.out.println("TransactionId \t Amount \t Date \t Type ");
									System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");	
									while(result.next())
									{
										System.out.println(result.getString("transactionId")+"\t"+result.getDouble("transactionAmount")+"\t"+result.getDate("transactiondate")+"\t"+result.getString("transactionType"));
									}
									System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");	
								}
								System.out.println("Do you want to continue??(Y/N)");
								status=br.readLine();
								System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				

								if(status.equals("n") || status.equals("N"))
								{
									login=false;
								}
								break;
								
								case 7:  login=false;
										 break;
								
									
										
								default:System.out.println("Wrong Input!!");		
											

							}
							
							
							
						}
							while(login);
							System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
							System.out.println("Bye.");
							System.out.println("Have a nice day!!");
							System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				

						}
						else
						{
							System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				
							System.out.println("Wrong username/secretpin!!");
							System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");				

						}
		}
	}
	}

	
						
						
					