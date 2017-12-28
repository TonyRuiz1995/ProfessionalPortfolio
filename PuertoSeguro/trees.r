library(stringr)
library(DMwR)
library(dplyr)
library(ggplot2)
library(tree)
library(randomForest)
library(resample)
library(rpart)
library(ROSE)
library(randomForest)
setwd("/home/tony/R/work/Puerto_kaggle_insurance")

df <-read.csv("train.csv")
str(df)
plots(df)
set.seed(123)
dummy_data <- sample_n(df,5000)

dummy.target <- ifelse(dummy_data$target == 1,"Yes", "No")
#ifelse(gender == "MALE", 1, ifelse(gender == "FEMALE", 2, 3))
dummy_data$dummy.target

cbind(dummy_data$target, dummy.target)

dummy_data <- data.frame(dummy_data, dummy.target)



dummy_data$id <-NULL #id had some affect on the tree
dummy_data$target <- NULL





##########################
#CLASSIFICATION TREE
##########################
set.seed(123)
dummy_data$dummy.target
train <- sample(1:nrow(dummy_data), nrow(dummy_data)/2)
tree.dummy <- tree(dummy.target ~., data = dummy_data[train,])
summary(tree.dummy)

tree.dummy

dummy.test <- dummy_data[-train, ]
dummy.test
tree.yhat <- predict(tree.dummy, newdata = dummy.test,type = "class" )
dummy.test.target <- dummy.test$dummy.target
dummy.test.target
cbind(tree.yhat, dummy.test.target)[1:20,]

table(tree.yhat, dummy.test.target)
table(tree.yhat)
table(dummy.test.target)
table(dummy_data[train,]$dummy.target)

#         dummy.test.target
#tree.yhat   No  Yes
#       No  2304   85
#       Yes   11    4



table(dummy_data[train,]$dummy.target)



set.seed(123)

#############################
#BAGGING
############################
set.seed(123)
bag.dummy <- randomForest(dummy.target ~., data = dummy_data[train,], mtry = 57)
bag.dummy

train

importance(bag.dummy)

varImpPlot(bag.dummy)
#TOP 5 IMPORTANT VARIABLES with Mean Decrease Gini
#ps_reg_03 : 8.9
#ps_car_13  : 8.426
#ps_calc_11 : 8.137
#ps_car_14 : 7.92
#ps_calc_14 : 7.59
yhat.bag <- predict(bag.dummy, newdata = dummy.test,type = "class")
table(yhat.bag) ### all no's 
cbind(yhat.bag, dummy.test.target)[1:20,]
table(yhat.bag, dummy.test.target)


#     dummy.test.target
#yhat.bag  No Yes
#   No  2415  85
#   Yes   0   0

roc.curve(dummy.test.target,yhat.bag)

set.seed(123)
##############################
#RANDOM FOREST
##############################
table(dummy_data$dummy.target)
rf.dummy <- randomForest(dummy.target ~., subset = train, data =dummy_data, mtry = 20)
rf.dummy
#Confusion matrix:
#  No Yes class.error
#No  240   0           0
#Yes  10   0           1

importance(rf.dummy)
varImpPlot(rf.dummy)
#####
#TOP 5 IMPORTANT VARIABLES
#ps_car_13  8.82
#ps_calc_11 8.25
#ps_reg_03  8.155
#ps_car_14  7.547
#ps_calc_14 7.495

yhat.rf <- predict(rf.dummy, newdata = dummy.test,type="class")
cbind(yhat.rf, dummy.test.target)[1:200,]
table(yhat.rf,dummy.test.target)
table(yhat.rf)
table(dummy.test.target)

length(yhat.rf)
length(dummy.test.target)


#dummy.test.target
#yhat.rf  No Yes
#   No  2415 85
#   Yes   0   0


roc.curve(dummy.test.target,yhat.rf)
##practically 
#AUC = .500
#####terribly OVERSAMPLED DATA






#####
#handling highly imbalanced data
#####

?ovun.sample

#undersampling 
data_balanced_under <- ovun.sample(dummy.target ~ ., data = dummy_data[train,], method = "under",N=2500, seed = 1)$data
table(data_balanced_under$dummy.target) #DIDNT REALLY BALANCE ANYTHING

#oversampling
data_balanced_over <- ovun.sample(dummy.target ~ ., data = dummy_data[train,], method = "both",N=2500, seed = 1)$data
table(data_balanced_over$dummy.target)


#BOTH UNDERSAMPLING AND OVERSAMPLING

data_balanced_both <- ovun.sample(dummy.target ~ ., data = dummy_data[train,], method = "both", p=0.5,N=2500, seed = 1)$data
table(data_balanced_both$dummy.target)

#0   1 
#1299 1201 

#USING SYNTHETICALLY GENERATED DATA (ROSE = RANDOM OVERSAMPLING EXAMPLES)
?ROSE
data_balanced_rose <- ROSE(dummy.target ~ ., data = dummy_data[train,] )$data
table(data_balanced_rose$dummy.target)
#0    1 
#1212 1288 


data_balanced_smote <- SMOTE(dummy.target ~ ., data=dummy_data[train,], perc.over = 1000, k = 5)
?SMOTE
table(data_balanced_smote$dummy.target) #number of observations != 2500
#No  Yes 
#1760  968 

tree.smote <- rpart(dummy.target ~., data = data_balanced_smote, method = "class")
#new tree on balanced data

tree.rose <- rpart(dummy.target ~ ., data = data_balanced_rose,method="class")
plot(tree.rose)
text(tree.rose, pretty = 1)
tree.both <- rpart(dummy.target ~ ., data = data_balanced_both,method = "class")
plot(tree.both)
text(tree.both, pretty = 1)
tree.under <-  rpart(dummy.target ~ ., data = data_balanced_under,method = "class")
text(tree.both, pretty = 1)
tree.over <-  rpart(dummy.target ~ ., data = data_balanced_over,method = "class")

#new predictions

pred.tree.smote <- predict(tree.smote, newdata = dummy_data[-train,], type= "class")
pred.tree.rose <- predict(tree.rose, newdata = dummy_data[-train,],type = "class")
pred.tree.under <- predict(tree.under, newdata = dummy_data[-train,],type="class")
pred.tree.over <- predict(tree.over, newdata = dummy_data[-train,], type= "class")

table(pred.tree.under)


dummy.test.target[1:200]
pred.tree.both

#               pred.tree.both
#dummy.test.target   No  Yes
#              No  1748  658
#              Yes   63   31

table(dummy.test.target,pred.tree.rose)

#               pred.tree.rose
#dummy.test.target   No  Yes
#             No     24 2391
#             Yes    2   83



table(dummy.test.target,pred.tree.smote)
length(pred.tree.rose)

#                   pred.tree.smote
#dummy.test.target   No  Yes
#               No  2373  64
#               Yes   63   0


table(dummy.test.target,pred.tree.over)
length(pred.tree.rose)

#                 pred.tree.over
#dummy.test.target   No  Yes
#               No  1676  761
#               Yes  42   21


table(dummy.test.target,pred.tree.under)

#                   pred.tree.under
#dummy.test.target   No  Yes
#               No  2415  0
#               Yes   85  0




table_accuracy= function(x,y){
  
  x = table(x,y)
  accuracy = (x[1,1] + x[2,2])
  return  (accuracy/2500)
  
}
table_accuracy(dummy.test.target, tree.yhat)
#[1] 0.9728
table_accuracy(dummy.test.target,pred.tree.over)
#[1] 0.6788
table_accuracy(dummy.test.target,pred.tree.under)
#[1] 0.9748
table_accuracy(dummy.test.target,pred.tree.smote)
#[1] 0.9492
table_accuracy(dummy.test.target,pred.tree.rose)
#[1] 0.0276


#ROC CURVES
roc.curve(dummy_data[-train,]$dummy.target, tree.yhat)
#Area under the curve (AUC): 0.501
roc.curve(dummy_data[-train,]$dummy.target, pred.tree.rose)
#Area under the curve (AUC): 0.501
roc.curve(dummy_data[-train,]$dummy.target, pred.tree.over)
#Area under the curve (AUC): 0.511
roc.curve(dummy_data[-train,]$dummy.target, pred.tree.under)
#Area under the curve (AUC): 0.500
roc.curve(dummy_data[-train,]$dummy.target, pred.tree.smote)
#Area under the curve (AUC): 0.513




###RESULTS FOR TREES
#highest roc curve for over sampling minority classification
#table had the most true positives but sacrificed accuracy

?randomForest.default

#####
#BAGGING
###
bag.dummy_under <- randomForest(dummy.target ~., data = data_balanced_under, mtry = 57)

yhat.bag_under <- predict(bag.dummy_under, newdata = dummy.test,type = "class")
table(yhat.bag_under, dummy.test.target)
table(yhat.bag_under)

bag.dummy_over <- randomForest(dummy.target ~., data = data_balanced_over, mtry = 57)

yhat.bag_over <- predict(bag.dummy_over, newdata = dummy.test,type = "class")
table(yhat.bag_over, dummy.test.target)



bag.dummy_rose <- randomForest(dummy.target ~., data = data_balanced_rose, mtry = 57)
yhat.bag_rose <- predict(bag.dummy_rose, newdata = dummy.test,type = "class")
table(yhat.bag_rose, dummy.test.target)


table(yhat.bag_rose)

table(data_balanced_rose$dummy.target)

table(dummy.test.target)
bag.dummy_smote <- randomForest(dummy.target ~., data = data_balanced_smote, mtry = 57, type="class")
bag.dummy_smote
yhat.bag_smote <- predict(bag.dummy_smote, newdata = dummy.test,type = "class")
table(yhat.bag_smote, dummy.test.target)


table_accuracy(dummy.test.target,yhat.bag)
table_accuracy(dummy.test.target,yhat.bag_smote)
table_accuracy(dummy.test.target,yhat.bag_rose)
table_accuracy(dummy.test.target,yhat.bag_under)
table_accuracy(dummy.test.target,yhat.bag_over)

#ROC CURVES
roc.curve(dummy.test.target, yhat.bag)
#Area under the curve (AUC): 0.513
roc.curve(dummy_data[-train,]$dummy.target, yhat.bag_smote)
#Area under the curve (AUC): 0.501
roc.curve(dummy_data[-train,]$dummy.target, yhat.bag_rose)
#Area under the curve (AUC): 0.501
roc.curve(dummy_data[-train,]$dummy.target, yhat.bag_over)
#Area under the curve (AUC): 0.505
roc.curve(dummy_data[-train,]$dummy.target, yhat.bag_under)
#Area under the curve (AUC): 0.500




####RESULTS WITH BAGGING
#ROSE HAD THE LOWEST TEST ACCURACY (0.0428) WITH THE HIGHEST 
#ROC CURVE (0.507)

#####
#random forest
###
rf.dummy_under <- randomForest(dummy.target ~., data = data_balanced_under, mtry = 7)

yhat.rf_under <- predict(rf.dummy_under, newdata = dummy.test,type = "class")
table(yhat.rf_under, dummy.test.target)


rf.dummy_over <- randomForest(dummy.target ~., data = data_balanced_over, mtry = 7)

yhat.rf_over <- predict(rf.dummy_over, newdata = dummy.test,type = "class")
table(yhat.rf_over, dummy.test.target)



rf.dummy_rose <- randomForest(dummy.target ~., data = data_balanced_rose, mtry = 7)

yhat.rf_rose <- predict(rf.dummy_rose, newdata = dummy.test,type = "class")
table(yhat.rf_rose, dummy.test.target)



rf.dummy_smote <- randomForest(dummy.target ~., data = data_balanced_smote, mtry = 7,type="class")

yhat.rf_smote <- predict(rf.dummy_smote, newdata = dummy.test,type = "class")

table(yhat.rf_smote, dummy.test.target)


table_accuracy(dummy.test.target,yhat.rf)
table_accuracy(dummy.test.target,yhat.rf_smote)
table_accuracy(dummy.test.target,yhat.rf_rose)
table_accuracy(dummy.test.target,yhat.rf_under)
table_accuracy(dummy.test.target,yhat.rf_over)

#ROC CURVES
roc.curve(dummy.test.target, yhat.rf)
#Area under the curve (AUC): 0.500
roc.curve(dummy.test.target, yhat.rf_smote)
#Area under the curve (AUC): 0.500
roc.curve(dummy.test.target, yhat.rf_rose)
#Area under the curve (AUC): 0.507
roc.curve(dummy.test.target, yhat.rf_under)
#Area under the curve (AUC): 0.500
roc.curve(dummy.test.target, yhat.rf_over)
#Area under the curve (AUC): 0.500




#BOOSTING
library(gbm)
is.numeric(dummy_data$target)

boost.dummy <-gbm(target ~., data = dummy_data[train, ], n.trees = 500)
yhat.boost<- predict(boost.dummy, newdata = dummy_data[-train, ], n.trees = 500, type = "response")
cbind(dummy_data[-train,]$target, yhat.boost)
table(yhat.boost)
#boosting just returned conditional probabilities 
#pretty much 3% chance of filing a claim across the board
yhat.boost


boost.01.pred <- rep(0, dim(dummy_data[-train,])[1])
boost.01.pred
boost.01.pred[yhat.boost>0.05]=1


models_trees <- list(original = model_rf,
              under = model_rf_under,
              over = model_rf_over,
              smote = model_rf_smote,
              rose = model_rf_rose)

resampling <- resamples(models)
bwplot(resampling)


###USING WEIGHTED RANDOM FOREST
library("wsrf")

wsrf <- wsrf(dummy.target ~., data = dummy_data[train,], parallel = FALSE)
table(dummy_data[train,]$dummy.target)
wsrf <- wsrf(dummy.target ~., data=dummy_data[train,], parallel = FALSE)
wsrf.yhat <- predict(wsrf, newdata= dummy.test,type = "class")$class
length(wsrf.yhat)
table(wsrf.yhat)
table(wsrf.yhat,dummy.test.target)

table(data_balanced_over$dummy.target)
over.wsrf <- wsrf(dummy.target ~., data= data_balanced_over, parallel = FALSE)
over.wsrf.yhat <- predict(over.wsrf, newdata= dummy.test,type = "class")$class
table(over.wsrf.yhat)
table(over.wsrf.yhat,dummy.test.target)



table(data_balanced_under$dummy.target)
under.wsrf <- wsrf(dummy.target ~., data= data_balanced_under, parallel = FALSE)
under.wsrf.yhat <- predict(under.wsrf, newdata= dummy.test,type = "class")$class
table(under.wsrf.yhat)
table(under.wsrf.yhat,dummy.test.target)

table(data_balanced_rose$dummy.target)
rose.wsrf <- wsrf(dummy.target ~., data= data_balanced_rose, parallel = FALSE)
rose.wsrf.yhat <- predict(rose.wsrf, newdata= dummy.test,type = "class")$class
table(rose.wsrf.yhat)
table(rose.wsrf.yhat,dummy.test.target)


table(data_balanced_smote$dummy.target)
smote.wsrf <- wsrf(dummy.target ~., data= data_balanced_smote, parallel = FALSE)
smote.wsrf.yhat <- predict(smote.wsrf, newdata= dummy.test,type = "class")$class
table(smote.wsrf.yhat)
table(smote.wsrf.yhat,dummy.test.target)



table_accuracy(dummy.test.target,wsrf.yhat)
table_accuracy(dummy.test.target,over.wsrf.yhat)
table_accuracy(dummy.test.target,under.wsrf.yhat)
table_accuracy(dummy.test.target,smote.wsrf.yhat)
table_accuracy(dummy.test.target,rose.wsrf.yhat)

#ROC CURVES
roc.curve(dummy_data[-train,]$dummy.target, wsrf.yhat)
#Area under the curve (AUC): 0.500
roc.curve(dummy_data[-train,]$dummy.target, rose.wsrf.yhat)
#Area under the curve (AUC): 0.508
roc.curve(dummy_data[-train,]$dummy.target, smote.wsrf.yhat )
#Area under the curve (AUC): 0.501
roc.curve(dummy_data[-train,]$dummy.target, under.wsrf.yhat )
#Area under the curve (AUC): 0.500
roc.curve(dummy_data[-train,]$dummy.target, over.wsrf.yhat)
#Area under the curve (AUC): 0.508


