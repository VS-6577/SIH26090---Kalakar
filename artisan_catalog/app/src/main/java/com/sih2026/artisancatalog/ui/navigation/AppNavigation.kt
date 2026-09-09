package com.sih2026.artisancatalog.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sih2026.artisancatalog.ui.screens.AddProductScreen
import com.sih2026.artisancatalog.ui.screens.AuthScreen
import com.sih2026.artisancatalog.ui.screens.DashboardScreen
import com.sih2026.artisancatalog.ui.screens.EstimatedPricingScreen
import com.sih2026.artisancatalog.ui.screens.GuidedCameraScreen
import com.sih2026.artisancatalog.ui.screens.LanguageSelectionScreen
import com.sih2026.artisancatalog.ui.screens.MarketScreen
import com.sih2026.artisancatalog.ui.screens.OnboardingPagerScreen
import com.sih2026.artisancatalog.ui.screens.PricingSummaryScreen
import com.sih2026.artisancatalog.ui.screens.ReviewScreen
import com.sih2026.artisancatalog.ui.screens.SettingsScreen
import com.sih2026.artisancatalog.ui.screens.WelcomeScreen
import com.sih2026.artisancatalog.ui.viewmodel.ProductViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {
        // SCREEN 1: Welcome Splash
        composable(
            route = Screen.Welcome.route,
            enterTransition = { fadeIn(tween(400)) },
            exitTransition = { fadeOut(tween(400)) }
        ) {
            WelcomeScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // SCREENS 2-5: Onboarding Pager
        composable(
            route = Screen.Onboarding.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
            }
        ) {
            OnboardingPagerScreen(
                onNavigateToLanguageSelection = {
                    navController.navigate(Screen.LanguageSelection.route)
                }
            )
        }

        // SCREEN 6: Language Selection
        composable(
            route = Screen.LanguageSelection.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
            }
        ) {
            LanguageSelectionScreen(
                viewModel = viewModel,
                onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route)
                }
            )
        }

        // AUTH: Login / Sign Up / OTP
        composable(
            route = Screen.Auth.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
            }
        ) {
            AuthScreen(
                viewModel = viewModel,
                onAuthComplete = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // DASHBOARD / HOME
        composable(
            route = Screen.Dashboard.route,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) }
        ) {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToAddProduct = {
                    navController.navigate(Screen.AddProduct.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToMarket = {
                    navController.navigate(Screen.Market.route)
                }
            )
        }

        // ADD PRODUCT (Step 1: Photo & Voice)
        composable(
            route = Screen.AddProduct.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(350))
            }
        ) {
            AddProductScreen(
                viewModel = viewModel,
                onNavigateToGuidedCamera = {
                    navController.navigate(Screen.GuidedCamera.route)
                },
                onNavigateToPricing = {
                    navController.navigate(Screen.EstimatedPricing.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToMarket = {
                    navController.navigate(Screen.Market.route)
                }
            )
        }

        // GUIDED CAMERA (6-View 3D Orientation Cube)
        composable(
            route = Screen.GuidedCamera.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(350))
            }
        ) {
            GuidedCameraScreen(
                viewModel = viewModel,
                onAllViewsCaptured = {
                    navController.popBackStack()
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }

        // ESTIMATED PRICING (Step 2: 4-Stage Wizard)
        composable(
            route = Screen.EstimatedPricing.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(350))
            }
        ) {
            EstimatedPricingScreen(
                viewModel = viewModel,
                onNavigateToReview = {
                    navController.navigate(Screen.Review.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Dashboard.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToMarket = {
                    navController.navigate(Screen.Market.route)
                }
            )
        }

        // REVIEW (Step 3: 6-Photo Carousel & Story)
        composable(
            route = Screen.Review.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(350))
            }
        ) {
            ReviewScreen(
                viewModel = viewModel,
                onNavigateToPricingSummary = {
                    navController.navigate(Screen.PricingSummary.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Dashboard.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToMarket = {
                    navController.navigate(Screen.Market.route)
                }
            )
        }

        // PRICING SUMMARY (Final Breakdown, Slider & Launch)
        composable(
            route = Screen.PricingSummary.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(350))
            }
        ) {
            PricingSummaryScreen(
                viewModel = viewModel,
                onLaunchSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Dashboard.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToMarket = {
                    navController.navigate(Screen.Market.route)
                }
            )
        }

        // SETTINGS & PROFILE
        composable(
            route = Screen.Settings.route,
            enterTransition = { fadeIn(tween(250)) },
            exitTransition = { fadeOut(tween(250)) }
        ) {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Dashboard.route)
                },
                onNavigateToMarket = {
                    navController.navigate(Screen.Market.route)
                },
                onNavigateToAddProduct = {
                    navController.navigate(Screen.AddProduct.route)
                },
                onNavigateToLanguageSelection = {
                    navController.navigate(Screen.LanguageSelection.route)
                },
                onLogOut = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // MARKETPLACE / LINKAGE
        composable(
            route = Screen.Market.route,
            enterTransition = { fadeIn(tween(250)) },
            exitTransition = { fadeOut(tween(250)) }
        ) {
            MarketScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Dashboard.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToAddProduct = {
                    navController.navigate(Screen.AddProduct.route)
                }
            )
        }
    }
}
