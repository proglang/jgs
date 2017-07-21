package end2endtest;

/**
 * Desribe how we want the fake analysis results the be
 */
enum FakeMethodTypings {
    ALL_DYNAMIC,                            // Var, Cx & Instantiation all return Dynamic on any request
    CX_PUBLIC,                              // same as ALL_DYNAMIC, except for Cx, which returns public on any request
    ALL_PUBLIC,                             // same as CX_PUBLIC, except for Var, which returns public on any request
    VAR_AND_CX_PUBLIC,                      // just instantiation dynamic

    CUSTOM_LowPlusPublic_AllDynamic,        // AllDynamic especially to test if typing map in CustomTyping.scala works
    CUSTOM_LowPlusPublic                    // Mapping such that i is Dynamic, j is public and res = i + j
}
