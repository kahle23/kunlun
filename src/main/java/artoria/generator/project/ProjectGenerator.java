package artoria.generator.project;

import artoria.generator.Generator;
import artoria.core.Context;

/**
 * Provide the highest level of abstraction for project generator.
 * @author Kahle
 */
public interface ProjectGenerator extends Generator {

    /**
     * Generate the corresponding project based on the project configuration.
     * @param projectConfig The configuration of the project to be built
     * @return The result of the project generation
     */
    Object generate(ProjectConfig projectConfig);

    /**
     * The context object at project generation time
     * @author Kahle
     */
    interface ProjectContext extends Context {

        /**
         * Get the project name.
         * @return The project name
         */
        String getName();

        /**
         * Set the project name.
         * @param name The project name
         */
        void setName(String name);

        /**
         * Get the project description.
         * @return The project description
         */
        String getDescription();

        /**
         * Set the project description.
         * @param description The project description
         */
        void setDescription(String description);

    }

}
