function p = predict(Theta1, Theta2, X)
%PREDICT Predict the label of an input given a trained neural network
%   p = PREDICT(Theta1, Theta2, X) outputs the predicted label of X given the
%   trained weights of a neural network (Theta1, Theta2)

% Useful values
m = size(X, 1);
num_labels = size(Theta2, 1);

% You need to return the following variables correctly 
p = zeros(size(X, 1), 1);

% ====================== YOUR CODE HERE ======================
% Instructions: Complete the following code to make predictions using
%               your learned neural network. You should set p to a 
%               vector containing labels between 1 to num_labels.
%
% Hint: The max function might come in useful. In particular, the max
%       function can also return the index of the max element, for more
%       information see 'help max'. If your examples are in rows, then, you
%       can use max(A, [], 2) to obtain the max for each row.
%

%size(X)
a1 = [ones(m, 1) X];
% X = 5000 400
% a1 = 5000 401
% a1' 401 5000
printf("size of a1 = %d\n", size(a1))
a2 = sigmoid(Theta1 * a1');
%theta1 = 25 * 401
%a2 = 25*5000
% we want to add a bias column, so transpose, add a column 
a2 = [ones(size(a2', 1), 1) a2'];
printf("size of a2 = %d\n", size(a2))
%a2 = 26 * 5000
a3 = sigmoid(Theta2 * a2');

%theta2 = 10 * 26
%a2     = 26 * 5000
%a3 = 10 * 5000

%now transpose a3' = 5000 * 10 

[v, p] = max(a3' , [], 2);
p(p == 10) = 0;








% =========================================================================


end
