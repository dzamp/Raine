package com.di.raine.branches;

/**
 * Created by jim on 11/9/2016.
 */

public class Comment {
    private String textMessage;
    private int rating;
    private int branchId;

    public Comment() {
    }

    public Comment(String textMessage, int rating, int branchId) {
        this.textMessage = textMessage;
        this.rating = rating;
        this.branchId = branchId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
}

/* Get comments
   networkService.requestComments(branches.get(position).first.getId(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject JsonResponse = null;
                            try {
                                 JsonResponse = new JSONObject(response);
                                JSONArray jsonComments  = JsonResponse.getJSONArray("data");
                                List<Comment> comments = new ArrayList<Comment>();
                                for (int i=0;i<jsonComments.length();i++) {
                                    JSONObject jsonComment = jsonComments.getJSONObject(i);
                                    Comment comm = new Gson().fromJson(jsonComment.toString(), Comment.class);
                                    comm.setBranchId(jsonComment.getJSONObject("branch").getInt("id"));
                                    comments.add(comm);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("hello");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG,error.getMessage());
                        }
                    });



                    // FIXME: 11/9/2016
                            networkService.postComment(branches.get(position).first.getId(), "Raine", 5, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("SUCCESFUL COMMENT", response);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    NetworkResponse response = error.networkResponse;
                                    if (error instanceof ServerError && response != null) {
                                        try {
                                            String res = new String(response.data,
                                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                            // Now you can use any deserializer to make sense of data
                                            JSONObject obj = new JSONObject(res);
                                        } catch (UnsupportedEncodingException e1) {
                                            // Couldn't properly decode data to string
                                            e1.printStackTrace();
                                        } catch (JSONException e2) {
                                            // returned data is not JSONObject?
                                            e2.printStackTrace();
                                        }
                                    }
                                }
                            });
*/


