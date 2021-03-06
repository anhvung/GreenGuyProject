# -*- coding: utf-8 -*-
"""
"""
# A partir du travail de Raymundo Cassani
import numpy as np

class Mlp():
    '''
    fully-connected Multi-Layer Perceptron (MLP)
    '''

    def __init__(self, size_layers, act_funct='sigmoid', reg_lambda=0, bias_flag=True):
        '''
        Constructor method. Defines the characteristics of the MLP
        Arguments:
            size_layers : List with the number of Units for:
                [Input, Hidden1, Hidden2, ... HiddenN, Output] Layers.
            act_funtc   : Activation function for all the Units in the MLP
                default = 'sigmoid'
            reg_lambda: Value of the regularization parameter Lambda
                default = 0, i.e. no regularization
            bias: Indicates is the bias element is added for each layer, but the output
        '''
        self.size_layers = size_layers
        self.n_layers    = len(size_layers)
        self.act_f       = act_funct
        self.lambda_r    = reg_lambda
        self.bias_flag   = bias_flag
 
        # Ramdomly initialize theta (MLP weights)
        self.initialize_theta_weights()

    def train(self, X, Y, iterations=400, reset=False):
        '''
        Given X (feature matrix) and y (class vector)
        Updates the Theta Weights by running Backpropagation N tines
        Arguments:
            X          : Feature matrix [n_examples, n_features]
            Y          : Sparse class matrix [n_examples, classes]
            iterations : Number of times Backpropagation is performed
                default = 400
            reset      : If set, initialize Theta Weights before training
                default = False
        '''
        n_examples = Y.shape[0]
#        self.labels = np.unique(y)
#        Y = np.zeros((n_examples, len(self.labels)))
#        for ix_label in range(len(self.labels)):
#            # Find examples with with a Label = lables(ix_label)
#           ix_tmp = np.where(y == self.labels[ix_label])[0]
#            Y[ix_tmp, ix_label] = 1

        if reset:
            self.initialize_theta_weights()
        for iteration in range(iterations):
            self.gradients = self.backpropagation(X, Y)
            self.gradients_vector = self.unroll_weights(self.gradients)
            self.theta_vector = self.unroll_weights(self.theta_weights)
            self.theta_vector = self.theta_vector - self.gradients_vector
            self.theta_weights = self.roll_weights(self.theta_vector)

    def predict(self, X):
        '''
        Given X (feature matrix), y_hay is computed
        Arguments:
            X      : Feature matrix [n_examples, n_features]
        Output:
            y_hat  : Computed Vector Class for X
        '''
        A , Z = self.feedforward(X)
        Y_hat = A[-1]
        return Y_hat

    def initialize_theta_weights(self):
        '''
        Initialize theta_weights, initialization method depends
        on the Activation Function and the Number of Units in the current layer
        and the next layer.
        The weights for each layer as of the size [next_layer, current_layer + 1]
        '''
        self.theta_weights = []
        size_next_layers = self.size_layers.copy()
        size_next_layers.pop(0)
        for size_layer, size_next_layer in zip(self.size_layers, size_next_layers):
            if self.act_f == 'sigmoid':
                # Method presented "Understanding the difficulty of training deep feedforward neurla networks"
                # Xavier Glorot and Youshua Bengio, 2010
                epsilon = 4.0 * np.sqrt(6) / np.sqrt(size_layer + size_next_layer)
                # Weigts from a uniform distribution [-epsilon, epsion]
                if self.bias_flag:  
                    theta_tmp = epsilon * ( (np.random.rand(size_next_layer, size_layer + 1) * 2.0 ) - 1)
                else:
                    theta_tmp = epsilon * ( (np.random.rand(size_next_layer, size_layer) * 2.0 ) - 1)            
            elif self.act_f == 'relu':
                # Method presented in "Delving Deep into Rectifiers: Surpassing Human-Level Performance on ImageNet Classfication"
                # He et Al. 2015
                epsilon = np.sqrt(2.0 / (size_layer * size_next_layer) )
                # Weigts from Normal distribution mean = 0, std = epsion
                if self.bias_flag:
                    theta_tmp = epsilon * (np.random.randn(size_next_layer, size_layer + 1 ))
                else:
                    theta_tmp = epsilon * (np.random.randn(size_next_layer, size_layer))                    
            self.theta_weights.append(theta_tmp)
        return self.theta_weights

    def backpropagation(self, X, Y):
        '''
        Implementation of the Backpropagation algorithm with regularization
        '''
        if self.act_f == 'sigmoid':
            g_dz = lambda x: self.sigmoid_derivative(x)
        elif self.act_f == 'relu':
            g_dz = lambda x: self.relu_derivative(x)

        n_examples = X.shape[0]
        # Feedforward
        A, Z = self.feedforward(X)

        # Backpropagation
        deltas = [None] * self.n_layers
        deltas[-1] = A[-1] - Y
        # For the second last layer to the second one
        for ix_layer in np.arange(self.n_layers - 1 - 1 , 0 , -1):
            theta_tmp = self.theta_weights[ix_layer]
            if self.bias_flag:
                # Removing weights for bias
                theta_tmp = np.delete(theta_tmp, np.s_[0], 1)
            deltas[ix_layer] = (np.matmul(theta_tmp.transpose(), deltas[ix_layer + 1].transpose() ) ).transpose() * g_dz(Z[ix_layer])

        # Compute gradients
        gradients = [None] * (self.n_layers - 1)
        for ix_layer in range(self.n_layers - 1):
            grads_tmp = np.matmul(deltas[ix_layer + 1].transpose() , A[ix_layer])
            grads_tmp = grads_tmp / n_examples
            if self.bias_flag:
                # Regularize weights, except for bias weigths
                grads_tmp[:, 1:] = grads_tmp[:, 1:] + (self.lambda_r / n_examples) * self.theta_weights[ix_layer][:,1:]
            else:
                # Regularize ALL weights
                grads_tmp = grads_tmp + (self.lambda_r / n_examples) * self.theta_weights[ix_layer]       
            gradients[ix_layer] = grads_tmp;
        return gradients