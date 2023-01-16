# [OTTO – Multi-Objective Recommender System](https://www.kaggle.com/competitions/otto-recommender-system)
+ Input feature 使用者過去的使用情形
  - session - the unique session id
  - events - the time ordered sequence of events in the session
    - aid - the article id (product code) of the associated event
    - ts - the Unix timestamp of the event
    - type - the event type, i.e., whether a product was clicked, added to the user's cart, or ordered during the session
+ Output feature
  - 分別預測clicks, carts, orders,最可能發生在哪20個商品
+ version
  - Gensim 版本為4.3.0
  - Approximate nearest neighbor 使用annoy 版本為1.17
+ Method
  - 首先，將資料轉為向量，使用gensim的word2vec模型將資料轉為向量
  - 把每一筆資料使用 Approximate nearest neighbor (ANN) 演算法建立模型
  - 在預測階段，先查詢訓練資料，如果Aid足夠20筆資料就直接輸出
  - 如果Aid小於20筆，就使用ANN模型找尋鄰近的點並輸出資料使Aid總數為20筆
+ Result
  - Score: 0.577
  - Ranking: 220/2216
<img src="https://github.com/gigilin7/Advanced_Data_Mining_and_Big_Data_Analysis/blob/main/Kaggle/score.jpg" width="800"/>

+ Reference

https://www.kaggle.com/code/balaganiarz0/word2vec-model-training-and-submission-0-533/notebook
