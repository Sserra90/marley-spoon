# Marley-spoon

Sérgio Serra

## Overall structure

There are 4 modules in the project:
- **net**, pure kotlin module
- **listing**, android feature module
- **base-android**, contains android resources and utils common to all feature modules
- **app module**

## Main libraries used

- Coroutines
- Airbnb Epoxy
- Navigation
- Hilt
- Glide
- Espresso

State management is done with a simplified version of unidirectional data flow using Kotlin flows and sealed classes for immutable and reactive state updates.

## Testing

The project contains an example on how to make unit and UI tests.
- Unit tests are available for repository and viewmodel
- UI test for RecipeListingFragment using Espresso and fragment scenario.