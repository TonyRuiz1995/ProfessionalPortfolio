import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from mpl_toolkits.mplot3d import Axes3D
from sklearn import datasets
from sklearn.decomposition import PCA
from sklearn.neighbors import KNeighborsClassifier
from sklearn.cross_validation import train_test_split
from sklearn.metrics import accuracy_score




# import some data to play with
iris = datasets.load_iris()

print "DATA"
print iris.data
print "TARGET"
print iris.target

iris_df = pd.DataFrame(data = iris.data)
iris_df['target'] = iris.target


#iris_df.rename(index=str, columns={0:"sepal_length", 1:"sepal_width", 2:"petal_length", 3:"petal_width"})
iris_df.columns = ['sepal_length','sepal_width','petal_length','petal_width','target']
print "DESCRIBING PANDAS DF"
print iris_df.describe()
print "HEAD"
print iris_df.head()

#print iris_df[iris_df.columns[0]] get first column
# Plot the training points
plt.scatter(iris_df[iris_df.columns[0]], iris_df[iris_df.columns[1]],c= iris_df['target'], edgecolor='k')
plt.xlabel('Sepal length')
plt.ylabel('Sepal width')

plt.show()

###TRY KNN
print " ----------------------------  "
X = np.array(iris_df.iloc[:,0:4])
Y = np.array(iris_df['target'])

print X
print "------------"
print Y
print "--------------"
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.33, random_state=42)

knn = KNeighborsClassifier(n_neighbors = 3)
# fitting the model
knn.fit(X_train, Y_train)

# predict the response
pred = knn.predict(X_test)

# evaluate accuracy
print "ACCURACY FOR KNN"
print accuracy_score(Y_test, pred) * 100



fig = plt.figure(1, figsize=(8, 6))
ax = Axes3D(fig, elev=-150, azim=110)
X_reduced = PCA(n_components=3).fit_transform(iris.data)
X_red =  pd.DataFrame(data = X_reduced)
X_red['target'] = iris_df['target']

ax.scatter(X_reduced[:, 0], X_reduced[:, 1], X_reduced[:, 2], c=iris_df['target'],
           cmap=plt.cm.Set1, edgecolor='k', s=40)
ax.set_title("First three PCA directions")
ax.set_xlabel("1st eigenvector")
ax.w_xaxis.set_ticklabels([])
ax.set_ylabel("2nd eigenvector")
ax.w_yaxis.set_ticklabels([])
ax.set_zlabel("3rd eigenvector")
ax.w_zaxis.set_ticklabels([])

'''
K-NearestNeighbors ON PCA
non-parametric method used for classification 
and regression. A sample is classified by a 
majority vote of its neighbors, with the
sample being assigned to the class most 
common among its k nearest neighbors 
(k is a positive integer, typically small). 
If k = 1, then the object is simply assigned
to the class of that single nearest neighbor.
'''

x = np.array(X_red.iloc[:,0:3])
y = np.array(X_red['target'])

x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.33, random_state=42)

knn = KNeighborsClassifier(n_neighbors = 3)
knn.fit(x_train, y_train)
Y_pred = knn.predict(x_test)
acc_knn = round(knn.score(x_train, y_train)*100, 2)
print "ACCURACY FOR PCA KNN"
print (acc_knn)


plt.show()


print X_red.head()
