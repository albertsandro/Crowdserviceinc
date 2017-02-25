package com.crowdserviceinc.crowdservice.parser;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.crowdserviceinc.crowdservice.util.http.HttpPostConnector;
import com.crowdserviceinc.crowdservice.model.BiddingDetail;
import com.crowdserviceinc.crowdservice.model.Category;
import com.crowdserviceinc.crowdservice.model.JobBean;
import com.crowdserviceinc.crowdservice.model.JobData;
import com.crowdserviceinc.crowdservice.model.JobsModel;
import com.crowdserviceinc.crowdservice.model.UserDetail;
import com.crowdserviceinc.crowdservice.util.Constants;

public class JobsPostParser {

	ArrayList<JobsModel> list = null;
	String strUrl;

	public JobsPostParser(String url) {
		// TODO Auto-generated constructor stub
		strUrl = url;
	}

	public ArrayList getParseData(ArrayList<NameValuePair> nameValueParams) {
		try {
			HttpPostConnector conn = new HttpPostConnector(strUrl,
					nameValueParams);
			String response = conn.getResponseData();
			Log.e("VIPVIPP", "URL RES > " + strUrl);
			Log.e("VIPVIPP", "RES Temp > " + conn.getResponseData());
			// Log.d("Courier Response", response);
			if (response != null) {
				String imageBasePath = "";

				JSONObject jsonresponse;
				try {
					jsonresponse = new JSONObject(response);
					list = new ArrayList<JobsModel>(1);
					JobsModel model = new JobsModel();
					if (jsonresponse.getString("status").equalsIgnoreCase(
							"success")) {
						model.setStatus("success");
						model.setMessage(jsonresponse.getString("message"));
						if (jsonresponse.has("image_path"))
							imageBasePath = jsonresponse
									.getString("image_path");

						if (jsonresponse.has("job_applied"))
							model.setJobApplied(jsonresponse
									.getString("job_applied"));
						if (jsonresponse.has("job_image"))
							model.setJobImage(jsonresponse
									.getString("job_image"));

						if (jsonresponse.has("checkbids")) {
							JSONArray array = jsonresponse
									.getJSONArray("checkbids");
							int c = array.length();
							String[] strA = new String[c];
							for (int i = 0; i < c; i++) {
								strA[i] = array.getString(i);
							}
							model.setCheckBids(strA);
						}

						// @Ashish
						if (jsonresponse.has("strsvalue")) {
							Constants.strsValue = jsonresponse
									.getString("strsvalue");
							Log.d("Constants.strsValue", ""
									+ Constants.strsValue);
						}
						// -Ashish-

						if (jsonresponse.has("Data_job")) {
							JSONArray jobArray = jsonresponse
									.getJSONArray("Data_job");
							int count = jobArray.length();
							JobData[] mJobData = new JobData[count];
							for (int i = 0; i < count; i++) {
								JSONObject jsonObject = jobArray
										.getJSONObject(i);

								JobData mData = new JobData();
								BiddingDetail bDetail = new BiddingDetail();
								if (jsonObject.has("0")) {
									JSONObject biddingJson = jsonObject
											.getJSONObject("0");

									if (biddingJson.has("count"))
										bDetail.setCount(biddingJson
												.getString("count"));
									if (biddingJson.has("link_status"))
										bDetail.setLinkStatus(biddingJson
												.getString("link_status"));
									if (biddingJson.has("distance"))
										bDetail.setDistance(biddingJson
												.getString("distance"));
									mData.setBidDetail(bDetail);
								}

								if (jsonObject.has("Bid")) {
									JSONObject biddingJson = jsonObject
											.getJSONObject("Bid");
									if (jsonObject.has("job_posted_by_id"))
										bDetail.setJobPostedById(biddingJson
												.getString("job_posted_by_id"));
									if (biddingJson.has("link_status"))
										bDetail.setLinkStatus(biddingJson
												.getString("link_status"));
									if (biddingJson.has("amount"))
										bDetail.setAmount(biddingJson
												.getString("amount"));
									if (biddingJson.has("id"))
										bDetail.setId(biddingJson
												.getString("id"));

								}

								mData.setBidDetail(bDetail);
								Log.e("VIPI", "Job Match 1>> ");
								if (jsonObject.has("Job")) {
									JobBean job = new JobBean();

									JSONObject jobJsonObject = jsonObject
											.getJSONObject("Job");

									job.setId(jobJsonObject.getString("id"));
									job.setName(jobJsonObject.getString("name"));
									job.setCategoryId(jobJsonObject
											.getString("category_id"));
									Log.e("VIPIII",
											"jobJsonObject 1 >> "
													+ jobJsonObject
															.getString("user_id"));
									job.setUserId(jobJsonObject
											.getString("user_id"));
									if(jobJsonObject.has("image"))
										job.setImage(jobJsonObject.getString("image"));
									job.setAmount(jobJsonObject
											.getString("amount"));
									if (jobJsonObject.has("start_latitude"))
										job.setStartLatitude(jobJsonObject
												.getString("start_latitude"));
									if (jobJsonObject.has("start_longitude"))
										job.setStartlongitude(jobJsonObject
												.getString("start_longitude"));
									job.setStartDate(jobJsonObject
											.getString("start_date"));
									job.setEndDate(jobJsonObject
											.getString("end_date"));
									if (jobJsonObject.has("from_location"))
										job.setFromLocation(jobJsonObject
												.getString("from_location"));
									if (jobJsonObject.has("to_location"))
										job.setToLocation(jobJsonObject
												.getString("to_location"));
									job.setDescription(jobJsonObject
											.getString("description"));
									job.setDate(jobJsonObject.getString("date"));
									job.setStatus(jobJsonObject
											.getString("status"));
									if (jobJsonObject.has("job_bid_status"))
										bDetail.setLinkStatus(jobJsonObject
												.getString("job_bid_status"));

									mData.setJob(job);
								}

								if (jsonObject.has("User")) {
									UserDetail user = new UserDetail();

									JSONObject userJsonObject = jsonObject
											.getJSONObject("User");

									Log.e("SAMPLE", " SAMPLE User 1<<<<>>>> "
											+ userJsonObject.getString("id")
											+ " IS_ " + Constants.is_customer);
//									if (!Constants.is_customer) {
//										Constants.tempuserid = userJsonObject
//												.getString("id");
//									}
									user.setId(userJsonObject.getString("id"));
									user.setUsername(userJsonObject
											.getString("username"));
									user.setFirst_name(userJsonObject
											.getString("first_name"));
									user.setLast_name(userJsonObject
											.getString("last_name"));

									if (userJsonObject.has("location"))
										user.setLocation(userJsonObject
												.getString("location"));
									Log.e("SAMPLE", "setUser >> 2 ");
									mData.setUser(user);
								}

								if (jsonObject.has("Category")) {
									Category category = new Category("", "");

									JSONObject catJsonObject = jsonObject
											.getJSONObject("Category");

									category.setId(catJsonObject
											.getString("id"));
									category.setName(catJsonObject
											.getString("name"));

									mData.setCategory(category);
								}

								mJobData[i] = mData;

							}

							model.setJobData(mJobData);
						}
						if (jsonresponse.has("Data_jobs")) {
							JSONArray jobArray = jsonresponse
									.getJSONArray("Data_jobs");
							int count = jobArray.length();
							JobData[] mJobData = new JobData[count];
							for (int i = 0; i < count; i++) {
								JSONObject jsonObject = jobArray
										.getJSONObject(i);

								JobData mData = new JobData();
								BiddingDetail bDetail = new BiddingDetail();
								if (jsonObject.has("0")) {
									JSONObject biddingJson = jsonObject
											.getJSONObject("0");

									if (biddingJson.has("count"))
										bDetail.setCount(biddingJson
												.getString("count"));
									if (biddingJson.has("link_status"))
										bDetail.setLinkStatus(biddingJson
												.getString("link_status"));
									if (biddingJson.has("distance"))
										bDetail.setDistance(biddingJson
												.getString("distance"));
									mData.setBidDetail(bDetail);
								}

								if (jsonObject.has("Bid")) {
									JSONObject biddingJson = jsonObject
											.getJSONObject("Bid");
									if (biddingJson.has("job_posted_by_id"))
										bDetail.setJobPostedById(biddingJson
												.getString("job_posted_by_id"));
									if (biddingJson.has("link_status"))
										bDetail.setLinkStatus(biddingJson
												.getString("link_status"));
									if (biddingJson.has("amount"))
										bDetail.setAmount(biddingJson
												.getString("amount"));

								}

								mData.setBidDetail(bDetail);
								Log.e("VIP", "Job Match 2>> ");
								if (jsonObject.has("Job")) {
									JobBean job = new JobBean();
									JSONObject jobJsonObject = jsonObject
											.getJSONObject("Job");
									job.setId(jobJsonObject.getString("id"));
									job.setName(jobJsonObject.getString("name"));
									if (jobJsonObject.has("start_date"))
										job.setStartDate(jobJsonObject
												.getString("start_date"));
									if (jobJsonObject.has("end_date"))
										job.setEndDate(jobJsonObject
												.getString("end_date"));
									mData.setJob(job);
								}

								if (jsonObject.has("User")) {
									UserDetail user = new UserDetail();
									JSONObject userJsonObject = jsonObject
											.getJSONObject("User");
									if (userJsonObject.has("id")) {
										user.setId(userJsonObject
												.getString("id"));
									}
									if (userJsonObject.has("first_name")) {
										user.setFirst_name(userJsonObject
												.getString("first_name"));
									}
									if (userJsonObject.has("last_name")) {
										user.setLast_name(userJsonObject
												.getString("last_name"));
									}
									if (userJsonObject.has("user_name")) {
										user.setUsername(userJsonObject
												.getString("user_name"));

									}

									if (userJsonObject.has("location"))
										user.setLocation(userJsonObject
												.getString("location"));
									Log.e("SAMPLE", "setUser >> 3 ");
									mData.setUser(user);
								}
								if (jsonObject.has("Category")) {
									Category category = new Category("", "");

									JSONObject catJsonObject = jsonObject
											.getJSONObject("Category");
									category.setId(catJsonObject
											.getString("id"));
									category.setName(catJsonObject
											.getString("name"));

									mData.setCategory(category);
								}

								mJobData[i] = mData;

							}

							model.setJobData(mJobData);
						}
						if (jsonresponse.has("Data_user")) {
							JobData[] mJobData = new JobData[1];
							JSONArray jsonArray = jsonresponse
									.getJSONArray("Data_user");

							UserDetail user = new UserDetail();

							JSONObject userJsonObject = jsonArray
									.getJSONObject(0).getJSONObject("User");

							user.setUsername(userJsonObject
									.getString("username"));

							if (userJsonObject.has("id")) {
								// Constants.tempuserid = userJsonObject
								// .getString("id");
								Log.e("SAMPLE", " SAMPLE Data_user 2<<<<>>>> "
										+ userJsonObject.getString("id")
										+ " IS_ " + Constants.is_customer);
								user.setId(userJsonObject.getString("id"));
//								if (Constants.is_customer) {
//									Constants.tempuserid = userJsonObject
//											.getString("id");
//								}
//								Log.d("Temp ID", Constants.tempuserid);
							}
							if (userJsonObject.has("first_name")) {
								user.setFirst_name(userJsonObject
										.getString("first_name"));
							}
							if (userJsonObject.has("last_name")) {
								user.setLast_name(userJsonObject
										.getString("last_name"));
							}
							if (userJsonObject.has("location"))
								user.setLocation(userJsonObject
										.getString("location"));
							//
							JobData jobdata = new JobData();
							Log.e("SAMPLE", "setUser >>");
							jobdata.setUser(user);

							if (jsonArray.getJSONObject(0).has("Bid")) {
								JSONObject biddingJson = jsonArray
										.getJSONObject(0).getJSONObject("Bid");
								BiddingDetail bDetail = new BiddingDetail();

								bDetail.setId(biddingJson.getString("id"));
								bDetail.setJobId(biddingJson
										.getString("job_id"));
								bDetail.setLinkStatus(biddingJson
										.getString("link_status"));
								bDetail.setAmount(biddingJson
										.getString("amount"));
								bDetail.setDate(biddingJson.getString("date"));

								jobdata.setBidDetail(bDetail);
							}

							mJobData[0] = jobdata;

							model.setJobData(mJobData);

						}
						if (jsonresponse.has("Data_users")) {
							JobData[] mJobData = null;
							JSONArray jsonArray = jsonresponse
									.getJSONArray("Data_users");
							if (jsonArray != null && jsonArray.length() > 0) {
								mJobData = new JobData[jsonArray.length()];
								for (int i = 0; i < jsonArray.length(); i++) {
									JobData jobdata = new JobData();
									if (jsonArray.getJSONObject(i).has("User")) {
										UserDetail user = new UserDetail();
										JSONObject userJsonObject = jsonArray
												.getJSONObject(i)
												.getJSONObject("User");
										user.setId(userJsonObject
												.getString("id"));
										if (userJsonObject.has("first_name"))
											user.setFirst_name(userJsonObject
													.getString("first_name"));
										if (userJsonObject.has("last_name"))
											user.setLast_name(userJsonObject
													.getString("last_name"));
										if (userJsonObject.has("username"))
											user.setUsername(userJsonObject
													.getString("username"));
										if (userJsonObject.has("avg_rating"))
											user.setAvg_rating(userJsonObject
													.getString("avg_rating"));
										if (userJsonObject.has("latitude"))
											user.setLatitude(userJsonObject
													.getString("latitude"));
										if (userJsonObject.has("longitude"))
											user.setLongitude(userJsonObject
													.getString("longitude"));
										//
										if (userJsonObject.has("location"))
											user.setLocation(userJsonObject
													.getString("location"));
										if (userJsonObject
												.has("security_shield"))
											user.setSecurity(userJsonObject
													.getString("security_shield"));
										//
										Log.e("SAMPLE", "setUser >> 1 ");
										jobdata.setUser(user);
									}
									if (jsonArray.getJSONObject(i).has("Bid")) {
										JSONObject biddingJson = jsonArray
												.getJSONObject(i)
												.getJSONObject("Bid");
										BiddingDetail bDetail = new BiddingDetail();
										// bDetail.setId(biddingJson.getString("id"));
										bDetail.setJobId(biddingJson
												.getString("job_id"));
										bDetail.setLinkStatus(biddingJson
												.getString("link_status"));
										bDetail.setAmount(biddingJson
												.getString("amount"));
										// bDetail.setDate(biddingJson.getString("date"));
										jobdata.setBidDetail(bDetail);
									}

									if (jsonArray.getJSONObject(i).has(
											"Jobprofile")) {
										JSONObject object = jsonArray
												.getJSONObject(i)
												.getJSONObject("Jobprofile");
										String img = object.getString("image");
										jobdata.setProfileImgUrl(imageBasePath
												+ img);
									}

									mJobData[i] = jobdata;
								}
							}

							model.setJobData(mJobData);

						}

					}

					else {
						model.setStatus("fail");
						model.setMessage(jsonresponse.getString("message"));
					}
					list.add(model);
				} catch (Exception ex) {
					Log.e("VIP", "Ex 1>>>" + ex.getMessage());
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("VIP", "Ex 2>>>" + e.getMessage());
		}
		return list;
	}
}
