import { createSlice } from '@reduxjs/toolkit';
import { authenticateUserData, filterProperties, 
    filterServices, getProperties, getServices, postProperty,postService } from './thunks/appThunk';

const initialState = {
    isUserLoggedIn: false,
    homeDialog:{
        isOpen:false,
        service:null,
    },
    homeDialogProperty:{
        isOpen:false,
        property:null,
    },
    currentTab: 0,
    properties: [],
    services: [],
    homePagePropertiesLoading: false,
    homePageServicesLoading: false,
    showToastMessageForPostService: false,
    showToastMessageForPostProperty: false
}

export const appSlice = createSlice({
    name: 'app',
    initialState,
    reducers: {
        updateUserLoggedInStatus: (state, action) => {
            state.isUserLoggedIn = action.payload.isUserLoggedIn;
        },
        authenticateUser: (state, action) => {
            state.isUserLoggedIn = action.payload.isUserLoggedIn;
        },
        logoutUser: (state, action) => {
            localStorage.removeItem("token");
            state.isUserLoggedIn = action.payload.isUserLoggedIn;
        },
        openModel: (state,action) => {
            state.homeDialog.isOpen = action.payload.homeDialog.isOpen;
            state.homeDialog.service = action.payload.homeDialog.service;
        },
        openModelProperty: (state,action) => {
            state.homeDialogProperty.isOpen = action.payload.homeDialogProperty.isOpen;
            state.homeDialogProperty.property = action.payload.homeDialogProperty.property;
        },
        setCurrentTab: (state, action) => {
            state.currentTab = action.payload.currentTab;
        }
    },
    extraReducers: (builder) => {
        builder.addCase(authenticateUserData.pending, (state) => {
            console.log('pending', state);
        });
        builder.addCase(authenticateUserData.fulfilled, (state, { payload }) => {
            if (payload.token) {
                state.isUserLoggedIn = true;
                state.token = payload.token;
                localStorage.setItem('token', payload.token);
                localStorage.setItem('userId', payload.userId);
                localStorage.setItem('username', payload.username);
            }
        });
        builder.addCase(authenticateUserData.rejected, (state, { payload }) => {
            console.log('rejected: ', payload);
            console.log('rejected');
        });

        builder.addCase(filterProperties.pending, (state) => {
            console.log('pending', state);
            state.homePagePropertiesLoading = true;
        });
        builder.addCase(filterProperties.fulfilled, (state, action) => {
            console.log('fulfilled: ', action);
            state.properties = [...action.payload];
            state.homePagePropertiesLoading = false;
        });
        builder.addCase(filterProperties.rejected, (state, { payload }) => {
            console.log('rejected: ', payload);
            console.log('rejected');
            state.homePagePropertiesLoading = false;
        });

        builder.addCase(filterServices.pending, (state) => {
            console.log('pending', state);
            state.homePageServicesLoading = true;
        });
        builder.addCase(filterServices.fulfilled, (state, action) => {
            console.log('fulfilled: ', action);
            state.services = [...action.payload];
            state.homePageServicesLoading = false;
        });
        builder.addCase(filterServices.rejected, (state, { payload }) => {
            console.log('rejected: ', payload);
            console.log('rejected');
            state.homePageServicesLoading = false;
        });

        builder.addCase(getProperties.pending, (state) => {
            console.log('pending', state);
            state.homePagePropertiesLoading = true;
        });
        builder.addCase(getProperties.fulfilled, (state, action) => {
            console.log('fulfilled: ', action);
            state.properties = [...action.payload];
            state.homePagePropertiesLoading = false;
        });
        builder.addCase(getProperties.rejected, (state, { payload }) => {
            console.log('rejected: ', payload);
            console.log('rejected');
            state.homePagePropertiesLoading = false;
        });

        builder.addCase(getServices.pending, (state) => {
            console.log('pending', state);
            state.homePageServicesLoading = true;
        });
        builder.addCase(getServices.fulfilled, (state, action) => {
            console.log('fulfilled: ', action);
            state.services = [...action.payload];
            state.homePageServicesLoading = false;
        });
        builder.addCase(getServices.rejected, (state, { payload }) => {
            console.log('rejected: ', payload);
            console.log('rejected');
            state.homePageServicesLoading = false;
        });

        builder.addCase(postService.pending, (state) => {
            console.log('pending', state);
            state.showToastMessageForPostService = true;
        });
        builder.addCase(postService.fulfilled, (state, action) => {
            console.log('fulfilled: ', action);
            state.services = [...state.services, { ...action.payload }];
            state.showToastMessageForPostService = false;
        });
        builder.addCase(postService.rejected, (state, { payload }) => {
            console.log('rejected: ', payload);
            console.log('rejected');
            state.showToastMessageForPostService = false;
        });
        // add builder for post property
        builder.addCase(postProperty.pending, (state) => {
            console.log('pending', state);
            state.showToastMessageForPostProperty = true;
        });
        builder.addCase(postProperty.fulfilled, (state, action) => {
            console.log('fulfilled: ', action);
            state.properties = [...state.properties, { ...action.payload }];
            state.showToastMessageForPostProperty = false;
        });
        builder.addCase(postProperty.rejected, (state, { payload }) => {
            console.log('rejected: ', payload);
            console.log('rejected');
            state.showToastMessageForPostProperty = false;
        });

    },
});

export const { 
    updateUserLoggedInStatus, authenticateUser, 
    logoutUser, openModel, 
    openModelProperty, setCurrentTab } = appSlice.actions;

export default appSlice.reducer;
