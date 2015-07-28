function [C, sigma] = dataset3Params(X, y, Xval, yval)
%EX6PARAMS returns your choice of C and sigma for Part 3 of the exercise
%where you select the optimal (C, sigma) learning parameters to use for SVM
%with RBF kernel
%   [C, sigma] = EX6PARAMS(X, y, Xval, yval) returns your choice of C and 
%   sigma. You should complete this function to return the optimal C and 
%   sigma based on a cross-validation set.
%

% You need to return the following variables correctly.
C = 1;
sigma = 0.3;

% ====================== YOUR CODE HERE ======================
% Instructions: Fill in this function to return the optimal C and sigma
%               learning parameters found using the cross validation set.
%               You can use svmPredict to predict the labels on the cross
%               validation set. For example, 
%                   predictions = svmPredict(model, Xval);
%               will return the predictions on the cross validation set.
%
%  Note: You can compute the prediction error using 
%        mean(double(predictions ~= yval))
%
best_err = 0,
values = [0.01, 0.03, 0.1, 0.3, 1, 3, 10, 30];
err_no = size(values, 2) * size(values, 2);
err_values = zeros(err_no, 1);
k = 1;
best_err = 10000;
for c=values;
    for s=values;
        model= svmTrain(X, y, c, @(x1, x2) gaussianKernel(x1, x2, s));
        pred = svmPredict(model , Xval);
        err = mean(double(pred ~= yval))
        err_values(k) = err;
        k = k + 1;
        if  err <= best_err,
            C = c; 
            sigma = s;
            best_err = err;
        end
        c
        s
        err
        best_err
    end
end
err_values
[val, index] = min(err_values(:))
c = values(idivide(index, size(values, 2)) + 1)
s = values(mod(index, size(values, 2)) + 1)
C
sigma
            





% =========================================================================

end
