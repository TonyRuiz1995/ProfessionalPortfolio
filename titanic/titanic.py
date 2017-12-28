import pandas as pd
import numpy as np
import random as rnd

# visualization
import seaborn as sns
import matplotlib.pyplot as plt

# machine learning
from sklearn.linear_model import LogisticRegression
from sklearn.svm import SVC, LinearSVC
from sklearn.ensemble import RandomForestClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn.linear_model import Perceptron
from sklearn.linear_model import SGDClassifier
from sklearn.tree import DecisionTreeClassifier


train_df = pd.read_csv('train.csv')
test_df = pd.read_csv('test.csv')
combine = [train_df, test_df]

##
print train_df.describe()
print(train_df.describe(include=['O']))
print train_df.head()
##
##how many males
print len(train_df[train_df['Sex']=='male'])

plt.hist(train_df['Pclass'])### quick histogram of Pclass
plt.show()


###Survival by  P Class??
#create a quick
table1 = pd.pivot_table(data=train_df, values='PassengerId', index='Pclass', columns='Survived', aggfunc='count')
print(table1)


# Array with the non survivors, divided by Pclass
bar_1 = table1[0] 
# Array with the survivors, divided by Pclass
bar_2 = table1[1]
# Range com a quantidade de itens das barras
x_pos = np.arange(len(bar_1))

first_bar = plt.bar(x_pos, bar_1, 0.5, color='b', label = "died")
second_bar = plt.bar(x_pos, bar_2, 0.5, color='y',label = "survived", bottom=bar_1)
# Definir position and labels for the X axis
plt.xticks(x_pos+0.25, ('1','2','3'))
plt.title(" By PClass")
plt.xlabel("Value")
plt.ylabel("Frequency")
plt.legend()
plt.show()


###Survival by Sex

table2 = pd.pivot_table(data=train_df, values='PassengerId', index='Sex', columns='Survived', aggfunc='count')
print(table2)


# Array with the non survivors, divided by Sex
bar_1 = table2[0] 
# Array with the survivors, divided by Sex
bar_2 = table2[1]
# Range com a quantidade de itens das barras
x_pos = np.arange(len(bar_1))

first_bar = plt.bar(x_pos, bar_1, 0.5, color='b',label = "died")
second_bar = plt.bar(x_pos, bar_2, 0.5, color='y',label = "survived", bottom=bar_1)
# Define position and labels for the X axis
plt.xticks(x_pos+0.25, ('Female','Male'))
plt.title("Survival By Sex")
plt.xlabel("Value")
plt.ylabel("Frequency")
plt.legend()
plt.show()


### dummy variable for sex
#1 if male, 0 if female
train_df['Sex'] = train_df['Sex'].astype('category')
train_df['d_sex'] = train_df['Sex'].cat.codes
test_df['Sex'] = test_df['Sex'].astype('category')
test_df['d_sex'] = test_df['Sex'].cat.codes


table3 = pd.pivot_table(data=train_df, values='PassengerId', index='Pclass', columns='d_sex', aggfunc='count')
###
### we get errors, SEX needs to be coded as a number?----yup
print(table3)

#Sex     female  male
#Pclass              
#1           94   122
#2           76   108
#3          144   347

# Array with Females, divided by Pclass
bar_1 = table3[0] 
# Array with the Males, divided by Pclass
bar_2 = table3[1]
# Range com a quantidade de itens das barras
x_pos = np.arange(len(bar_1))

first_bar = plt.bar(x_pos, bar_1, 0.5, color='b', label = "Female")
second_bar = plt.bar(x_pos, bar_2, 0.5, color='y', label = "Male",bottom=bar_1)
# Definir position and labels for the X axis
plt.xticks(x_pos+0.25, ('Class 1','Class 2','Class3'))
plt.legend()
plt.title("Sex By PClass")
plt.xlabel("Value")
plt.ylabel("Frequency")
plt.show()




###RESULTS 
# women lived more
# Class 3 was screwed


##PLAY AROUND WITH AGE
#Create a dummy variable for child 
train_df['is_child'] = np.where(train_df["Age"] < 18, 1, 0)
test_df['is_child'] = np.where(test_df["Age"] < 18, 1, 0)

###Survival by Sex

table4 = pd.pivot_table(data=train_df, values='PassengerId', index='is_child', columns='Survived', aggfunc='count')
print(table2)


# Array with the non survivors, divided by Sex
bar_1 = table2[0] 
# Array with the survivors, divided by Sex
bar_2 = table2[1]
# Range com a quantidade de itens das barras
x_pos = np.arange(len(bar_1))

first_bar = plt.bar(x_pos, bar_1, 0.5, color='b',label = "died")
second_bar = plt.bar(x_pos, bar_2, 0.5, color='y',label = "survived", bottom=bar_1)
# Definir position and labels for the X axis
plt.xticks(x_pos+0.25, ('child','not a child'))
plt.title("Survival By is_child")
plt.xlabel("Value")
plt.ylabel("Frequency")
plt.legend()
plt.show()

###
## 300 children to ~600 adults
## children stand a significant chance of living 
train_df.dropna()
test_df.dropna()
train_df = train_df.drop(['PassengerId','Ticket', 'Cabin','Sex','Name','Age','Cabin','Embarked'], axis=1)
test_df = test_df.drop(['PassengerId','Ticket', 'Cabin','Sex','Name','Age','Cabin','Embarked'], axis=1)
X_train = train_df.drop("Survived", axis=1)

Y_train = train_df["Survived"] ###catergorical feature you want to test

X_test  = test_df
X_test.iloc[152,3] = np.mean(X_test['Fare']) #we had an error here, had to impute with the mean

##logistic regression
logreg = LogisticRegression()
logreg.fit(X_train, Y_train)


print "------X TRAIN----"
print X_train.describe()
print "------X Test----"
print X_test.describe()
Y_pred = logreg.predict(X_test)
acc_log = round(logreg.score(X_train, Y_train) * 100, 2)
print "ACCURACY"
print (acc_log)
### got an 80% accuracy 
# Can we do better?





