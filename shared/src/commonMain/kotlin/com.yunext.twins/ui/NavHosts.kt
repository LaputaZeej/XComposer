//package com.yunext.twins.ui
//
//// 从此 Codelab 的开头开始，您就确保不将 navController 直接传入任何可组合项（高级应
//// 用除外），而是将导航回调作为形参传递。这样一来，您的所有可组合项均可单独测试，因为它们
//// 不需要在测试中使用 navController 实例。
////@Composable
////fun CHNavHost(
////    navController: NavHostController,
////    modifier: Modifier = Modifier,
////) {
////    NavHost(
////        navController = navController,
////        startDestination = Empty.route,
////        modifier = modifier
////    ) {
////
////        composable(route = HomeDestination.route){
////            HomeScreen(list = , onDeviceSelected = )
////        }
//////        composable(route = X1.route) {
//////            X1Screen(onClickBtn1 = {
//////                navController.navigateSingleTopTo(X2.route)
//////            }, onClickBtn2 = {
//////                navController.navigateSingleTopTo(X3.route)
//////            }, onItemClick = { accountType ->
//////                //navController.navigateSingleTopTo("${SingleX.route}/$accountType")
//////                navController.navigateToSingleX(accountType)
//////            })
////
//////        }
////
//////        composable(route = X2.route) {
//////            X2Screen() { accountType ->
//////                //navController.navigateSingleTopTo("${SingleX.route}/${accountType}")
//////                navController.navigateToSingleX(accountType)
//////            }
//////        }
//////
//////        composable(route = X3.route) {
//////            X3Screen()
//////        }
//////
//////        composable(
//////            route = SingleX.routeWithArgs, arguments = SingleX.arguments,
//////            deepLinks = SingleX.deepLinks
//////        ) { navBackStackEntry ->
//////            val accountType = navBackStackEntry.arguments?.getString(SingleX.accountTypeArg)
//////            SingleXScreen(accountType)
//////        }
////    }
////}
//
//// https://developer.android.com/codelabs/jetpack-compose-navigation?hl=zh-cn&continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fcompose%3Fhl%3Dzh-cn%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-navigation#4
//
//fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route)
//fun NavHostController.navigateSingleTopTo2(route: String) =
//    this.navigate(route) {
//        // Pop up to the start destination of the graph to
//        // avoid building up a large stack of destinations
//        // on the back stack as users select items
//        // 弹出到导航图的起始目的地，以免在您选择标签页时在返回堆栈上积累大量目的地
//        // 在任何目的地按下返回箭头都会将整个返回堆栈弹出到“X1”屏幕
//        popUpTo(
//            this@navigateSingleTopTo2.graph.findStartDestination().id
//        ) {
//            saveState = true
//            //this.inclusive = true
//        }
//        // Avoid multiple copies of the same destination when
//        // reselecting the same item
//        // 这可确保返回堆栈顶部最多只有给定目的地的一个副本
//        launchSingleTop = true
//        // Restore state when reselecting a previously selected item
//        // 确定此导航操作是否应恢复 PopUpToBuilder.saveState 或 popUpToSaveState 属性之前保存的任何状态。请注意，如果之前未使用要导航到的目的地 ID 保存任何状态，此项不会产生任何影响
//        //在 Rally 中，这意味着，重按同一标签页会保留屏幕上之前的数据和用户状态，而无需重新加载
//        restoreState = true
//    }